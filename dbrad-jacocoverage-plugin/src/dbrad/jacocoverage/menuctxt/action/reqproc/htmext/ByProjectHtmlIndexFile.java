/* Copyright (c) 2017 dbradley. All rights reserved. */
package dbrad.jacocoverage.menuctxt.action.reqproc.htmext;

import dbrad.jacocoverage.plugin.util.NbUtilsPlugin;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Class used to create a by-project HTML index file for presentation to the
 * user of dbrad-jacocoverage reports. This class will combine the access to the
 * separate project HTML reports via the by-product presentation.
 *
 * @author dbradley
 */
public class ByProjectHtmlIndexFile {

    private final String rootProjectName;
    private final String timeStampFormatted;
    private final File reportIntoDirectory;
    private final ArrayList<String> projectIndexFilesList;

    ArrayList<String> htmlLineListArr = new ArrayList<>();

    // - - - - data shared across HTML tag sections
    private String firstHtmlSrcFile;

    /**
     * Create the index.html file for a by-product report.
     *
     * @param rootProjectName       string of the root project name for the
     *                              report
     * @param timeStampFormatted    the time-stamp the report was created
     * @param reportIntoDirectory   the directory that the by-project index.html
     *                              report will be placed into
     * @param projectIndexFilesList the projects list index.html files to be
     *                              processed into the by-product index.html
     */
    public ByProjectHtmlIndexFile(
            String rootProjectName,
            String timeStampFormatted,
            File reportIntoDirectory,
            ArrayList<String> projectIndexFilesList) {

        this.rootProjectName = rootProjectName;
        this.timeStampFormatted = timeStampFormatted;
        this.reportIntoDirectory = reportIntoDirectory;
        this.projectIndexFilesList = projectIndexFilesList;
    }

    /**
     * Get the percentage of the instruction executed from a an single project's
     * index.html file (the total line).
     *
     * @param projectIndexFileStrthe the project index.html file to be processed
     *                               for the percentage value
     *
     * @return string of the percentage, or '----' if unavailable
     */
    @SuppressWarnings("CallToPrintStackTrace")
    private String getTotalPercentage(String projectIndexFileStr) {

        String percentStr = "----";  // error string format
        String indexFileContent = "";

        // read the project index.html from to internal buffer and then
        // process to get the percentage string
        BufferedReader bReader = null;
        FileReader fReader = null;

        try {
            fReader = new FileReader(projectIndexFileStr);
            bReader = new BufferedReader(fReader);

            String sCurrentLine;
            while ((sCurrentLine = bReader.readLine()) != null) {
                indexFileContent += sCurrentLine;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return percentStr;

        } finally {
            try {
                if (bReader != null) {
                    bReader.close();
                }
                if (fReader != null) {
                    fReader.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                return percentStr;
            }
        }
        // process the footer to get the coverage percentage
        int tfootEndIdx = indexFileContent.lastIndexOf("/tfoot>");
        if (tfootEndIdx != -1) {
            int tfootIdx = indexFileContent.lastIndexOf("tfoot>", tfootEndIdx);
            if (tfootIdx != -1) {
                String extractTfoot = indexFileContent.substring(tfootIdx, tfootEndIdx);

                // look for the percentage symbol
                int percentIdx = extractTfoot.indexOf("%");
                if (percentIdx != -1) {
                    int percentBackIdx = extractTfoot.lastIndexOf("ctr2\">", percentIdx);
                    if (percentBackIdx != -1) {
                        percentStr = extractTfoot.substring(percentBackIdx + 6, percentIdx + 1);
                    }
                }
            }
        }
        return percentStr;
    }

    /**
     * Create the by-product index.html file into the report-into-directory.
     * <p>
     * Builds the HTML code for the index.html file.
     *
     * @return File of the index.html for launch browser window (if option is
     *         set)
     *
     * @throws IOException if there is some form of IO issue
     */
    public File createTheFile() throws IOException {
        //   <html>
        //    <head>
        //        <title>dbrad-jacocoverage</title>
        //        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        //    </head>
        //    <body>
        //       :
        //    </body>
        //   </html>
        //
        HtmlTag html = new HtmlTag("html");
        html.appendTag(buildHead());
        html.appendTag(buildBody());

        // prepare to create the index.hmtl file
        String documentContent = String.format("<!DOCTYPE html>\n%s", html.toString());

        // create the output file for the base index file
        File createIndexHtmlFile = new File(this.reportIntoDirectory, "index.html");

        try (
                PrintWriter outputStream = new PrintWriter(new FileWriter(createIndexHtmlFile))) {
            outputStream.println(documentContent);
            outputStream.flush();
        }
        return createIndexHtmlFile;
    }

    /**
     * Build the HEAD tag section of the document.
     *
     * @param html the HTML document tag
     */
    private HtmlTag buildHead() {
        HtmlTag head = new HtmlTag("head");

        // document title
        String titleStr = String.format("%s @ %s",
                this.rootProjectName,
                this.timeStampFormatted);

        HtmlTag title = new HtmlTag("title");
        title.appendContentText(titleStr);
        head.appendTag(title);

        // meta data
        head.appendContentText("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");

        // styles and scripts add
        includeStyles(head);
        includeScripts(head);

        return head;
    }

    /**
     * Build the BODY tag section of the document.
     *
     * @param html the HTML document tag
     */
    private HtmlTag buildBody() {
        HtmlTag body = new HtmlTag("body");

        body.appendTag(buildHeading());
       
        HtmlTag divTable = new HtmlTag("div");
        divTable.appendAttr(new AttrItem("class", "tTable"));
        body.appendTag(divTable);

        // the display is a div-table of a single row
        //
        // left-cell) contents are a list of all the projects
        //
        // right-cell) content is an iframe of a project's coverage report
        //
        //
        HtmlTag divRow = new HtmlTag("div");
        divRow.appendAttr(new AttrItem("class", "tRow"));
        divTable.appendTag(divRow);
        
        // - - - left
        HtmlTag divPrjListContent = buildListOfProjectDivContent();
        divPrjListContent.appendAttr(new AttrItem("class", "tCellLeft"));
        
        divRow.appendTag(divPrjListContent);

        // - - - right
        HtmlTag divIframeContent = buildIframeForProjectReportToDisplayDiv();
        divIframeContent.appendAttr(new AttrItem("class", "tCellRight"));
        
        divRow.appendTag(divIframeContent);

        return body;
    }

    private HtmlTag buildHeading() {
        // add a heading for the report
        String headingStr = String.format("Jacoco by-project for: %s @ %s",
                this.rootProjectName,
                this.timeStampFormatted);

        HtmlTag h1Tag = new HtmlTag("h1");
        h1Tag.appendContentText(headingStr);

        return h1Tag;
    }

    private HtmlTag buildListOfProjectDivContent() {
        HtmlTag divCellLeft = new HtmlTag("div");

        // put in a heading for the report
        String reportIntoDirString = reportIntoDirectory.getAbsolutePath();
        int lenReportIntoDir = reportIntoDirString.length();

        // build the left side cell content
        HtmlTag pExplain = new HtmlTag("p");
        pExplain.appendContentText("The following list links to each project &amp;"
                + " source-code folder group of data:");

        divCellLeft.appendTag(pExplain);

        // each project will be a li item that can be select or accessibility 
        // selected via tab-focus
        HtmlTag ul = new HtmlTag("ul");
        ul.appendAttr(new AttrItem("class", "ulnoindent"));
        divCellLeft.appendTag(ul);

        // the first item will be displayed in the iframe element, and also
        // hi-lited with black vs the links being blue
        this.firstHtmlSrcFile = null;

        // for each project index.html file need a li item created
        for (String indexLine : projectIndexFilesList) {
            String percentStr = String.format("%4s", getTotalPercentage(indexLine));
            percentStr = percentStr.replaceAll(" ", "&nbsp;");

            indexLine = NbUtilsPlugin.convertPathToFwdSlash(indexLine);
            //
            HtmlTag liTag = new HtmlTag("li");
            ul.appendTag(liTag);

            // formatting and action tags for link
            // actions: 'onclick' or 'onkeypress of cr/lf/space' change the iframe
            // src attribute
            liTag.appendAttr(new AttrItem("class", "lilist"));
            liTag.appendAttr(new AttrItem("tabindex", "0"));
            liTag.appendAttr(new AttrItem("onclick", "changeIframe(this);"));
            liTag.appendAttr(new AttrItem("onkeypress", "typeIframe(this, event);"));

            // the data- which is the project link relative address to be used
            // by the function calls for the key-press and click actions
            String projLinkRelative = String.format(".%s", indexLine.substring(lenReportIntoDir));

            liTag.appendAttr(new AttrItem("data-link", projLinkRelative));

            // the first item needs to be the initial iframe and the li-item
            // is hi-lited
            if (this.firstHtmlSrcFile == null) {
                this.firstHtmlSrcFile = projLinkRelative;
                liTag.appendAttr(new AttrItem("style", "color: black;"));
            } else {
                liTag.appendAttr(new AttrItem("style", "color: blue;"));
            }
            // extract the report name to be the hi-lited/link item content-text
            String projSrcFolderLinkContext = projLinkRelative.substring(
                    projLinkRelative.indexOf("/") + 1,
                    projLinkRelative.lastIndexOf(".report.html"));

            // the instruction percentage pass rate and a link connection
            // that may be clicked or key-pressed
            projSrcFolderLinkContext = String.format(
                    "<span class='pctstr'>%s</span> "
                    + "<span class='linkcons'>%s</span>",
                    percentStr,
                    projSrcFolderLinkContext);
            liTag.appendContentText(projSrcFolderLinkContext);
        }
        return divCellLeft;
    }

    private HtmlTag buildIframeForProjectReportToDisplayDiv() {
        HtmlTag divCellRight = new HtmlTag("div");

        // build the right side cell content
        HtmlTag iframeTag = new HtmlTag("iframe");
        iframeTag.appendAttr(new AttrItem("class", "riframe"));
        iframeTag.appendAttr(new AttrItem("id", "riframeitem"));
        iframeTag.appendAttr(new AttrItem("src", this.firstHtmlSrcFile));
        iframeTag.appendAttr(new AttrItem("onload", "resizeIframe(this);"));

        divCellRight.appendTag(iframeTag);

        return divCellRight;
    }

    /**
     * The styles tag code for CSS settings of the index.html file. (In-line
     * rather than as a CSS file.)
     */
    private static final String[] STYLES
            = new String[]{
                ".tTable {display: table;}",
                ".tRow {display: table-row;}",
                ".tCellLeft {display: table-cell; vertical-align: top; width: 25%; max-width: 220px;}",
                ".tCellRight {display: table-cell; vertical-align: top;}",
                ".pctstr {font-family: courier; font-size: 0.95em;}",
                ".linkcons {text-decoration: underline;}",
                ".riframe {position: absolute; width: 73%;height: 85%;}",
                ".ulnoindent {padding-left: 1.2em;}",
                ".lilist {cursor:pointer;}",
                "body, td {font-family:sans-serif; font-size:10pt;}",
                "h1 {font-weight:bold;  font-size:18pt;}"
            };

    /**
     * Include the styles HTML code to the HEAD tag.
     *
     * @param headTag the head-tag to include into
     */
    private void includeStyles(HtmlTag headTag) {
        HtmlTag styleTag = new HtmlTag("style");

        for (String s : STYLES) {
            styleTag.appendContentText(s);
        }
        headTag.appendTag(styleTag);
    }

    /**
     * The scripts tag code for CSS settings of the index.html file. (In-line
     * rather than as a CSS file.)
     */
    private static final String[] SCRIPTS
            = new String[]{
                // onclick explcit or a key-press forward action
                // of a selection item
                "function changeIframe(thisEle) {",
                // set the iframe to a new selection by getting the
                // data-link attribute (the relative path to the selection
                // item index.html file)
                "  var nuIframeSrc = thisEle.getAttribute('data-link');",
                "  var iframeEle = document.getElementById('riframeitem');",
                "  iframeEle.setAttribute('src', nuIframeSrc);",
                "  ",
                // change the color of all list items to a link blue color
                // and then black for the selected item
                "  var liItemsArr = document.getElementsByClassName('lilist');",
                "  for(i = 0; i < liItemsArr.length; i++){",
                "    liItemsArr[i].style.color = 'blue';",
                "  }",
                "  thisEle.style.color = 'black';",
                "};",
                " ",
                // key-press action checks for carriage-return, linefeed or space as
                // change the iframe to the selection action
                "function typeIframe(thisEle, evt) {",
                "  if(evt != undefined) { ",
                "    var c = evt.which; ",
                "    if (c === 13 || c === 10 || c == 32) {",
                "      changeIframe(thisEle);",
                "    }",
                "  }",
                "};"
            };

    /**
     * Include the scripts HTML code to the HEAD tag.
     *
     * @param headTag the head-tag to include into
     */
    private void includeScripts(HtmlTag headTag) {
        HtmlTag scriptTag = new HtmlTag("script");

        for (String s : SCRIPTS) {
            scriptTag.appendContentText(s);
        }
        headTag.appendTag(scriptTag);
    }
}
