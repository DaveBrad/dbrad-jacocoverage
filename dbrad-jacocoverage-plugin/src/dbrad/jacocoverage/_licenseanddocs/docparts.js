/* Copyright (c) 2017 dbradley. */

/**
 * Dbrad-jacocoverage has a number of sections of documentation and
 * are presented at the top of each section allowing access to all docs.
 * 
 * This script applies the doc index and also provides a copyright too.
 */
function applyDocIndex() {
  var docIndexEle = document.getElementById("docindexcpyrght");

  applyCopyright();

  // document heading information
  var styleEle = document.createElement("style");
  var sectlistStyle = ".sectlist {padding-left: 20px; text-align: center;}";
  sectlistStyle += ".sectlist th {border-bottom: 1px solid}";
  sectlistStyle += ".sectlist td {padding-left: 2ch;padding-right: 2ch;}";

  styleEle.innerHTML = sectlistStyle;
  //
  // <style>padding-left: 20px; text-align: center;
  // .sectlist th {border-bottom: 1px solid}
  // .sectlist td {padding-left: 2ch;padding-right: 2ch;}
  // 
  // </style>
  // <table class="sectlist">
  //  <tr>
  //  <th colspan="3">documentation</th>
  //  </tr>
  //  <tr>
  //   <td><a href="./intro.html">Introduction dbrad-jacocoverage</a></td>
  //   <td><a href="./global.html">Global settings</a></td>
  //   <td><a href="./prjspec.html">Project specific settings</a></td>
  //  </tr>
  //  <tr>
  //   <td><a href="../_licenseanddocs/_license_dbrad.html">License</a></td>
  //   <td colspan="2"><a href="./reportdesc.html">Reports brief</a></td>
  //  </tr>
  // </table>
  // <p>Configuration profile is stored in '{project}/.jacocodbrad' folder, it is
  //  advised that the folder and/or contents be set ignored in any 
  //  source-code-manager/version-control-source managed projects.</p>
  var tableEle = document.createElement("table");
  tableEle.setAttribute("class", "sectlist");
  //
  var tr1Ele = document.createElement("tr");
  var thEle = document.createElement("th");
  thEle.setAttribute("colspan", "3");
  thEle.innerHTML = "dbrad-jacocoverage documentation";

  tr1Ele.appendChild(thEle);

  tableEle.appendChild(tr1Ele);
  //
  var tr2Ele = document.createElement("tr");
  //   <td><a href="./intro.html">Introduction dbrad-jacocoverage</a></td>
  //   <td><a href="./global.html">Global settings</a></td>
  //   <td><a href="./prjspec.html">Project specific settings</a></td>
  tr2Ele.appendChild(tdWithAtag("./intro.html", "Introduction"));
  tr2Ele.appendChild(tdWithAtag("./global.html", "Global settings"));
  tr2Ele.appendChild(tdWithAtag("./prjspec.html", "Project specific settings"));

  tableEle.appendChild(tr2Ele);
  //
  var tr3Ele = document.createElement("tr");
  //   <td><a href="./_license_dbrad.html">License</a></td>
  //   <td colspan="2"><a href="./reportdesc.html">Reports brief</a></td>
  tr3Ele.appendChild(tdWithAtag("./_license_dbrad.html", "License"));

  var td23tr3Ele = tdWithAtag("./reportdesc.html", "Reports brief");
  td23tr3Ele.setAttribute("colspan", "2");

  tr3Ele.appendChild(td23tr3Ele);

  tableEle.appendChild(tr3Ele);
  //
  var pDirEle = document.createElement("p");
  pDirEle.innerHTML = "A configuration profile is stored in '{project}/.jacocodbrad' folder, it is"
          + " advised that the folder and/or contents be set ignored in any"
          + " source-code-manager/version-control-source managed projects.";
  // 
  docIndexEle.appendChild(styleEle);
  docIndexEle.appendChild(tableEle);
  docIndexEle.appendChild(pDirEle);
}

function tdWithAtag(hrefVal, aStr) {
  var tdEle = document.createElement("td");
  tdEle.appendChild(aTagAppend(hrefVal, aStr));
  return tdEle;
}

function aTagAppend(hrefVal, aStr) {
  var aEle = document.createElement("a");
  aEle.setAttribute("href", hrefVal);
  aEle.innerHTML = aStr;
  return aEle;
}

/**
 * As part of the document parts introduce a footer copyright
 * message that will always be showing.
 */
function applyCopyright() {
  var bodyEle = document.getElementsByTagName("body");

  bodyEle[0].appendChild(document.createElement("br"));
  bodyEle[0].appendChild(document.createElement("br"));

  var styleEle = document.createElement("style");
  styleEle.innerHTML = ".cpyrght{"
          + "position: fixed; left: 5px; bottom: 0px;"
          + "background: white; width: 98%; height: 2em; background: white;"
          + "font-size: 0.8em; text-align: center; padding-top: 8px;}";

  bodyEle[0].appendChild(styleEle);

  var divEle = document.createElement("div");
  divEle.setAttribute("id", "cpyrghtid");
  divEle.setAttribute("class", "cpyrght bottomfixed");
  divEle.innerHTML = "Copyright &copy; 2017-2018 dbradley.";
  bodyEle[0].appendChild(divEle);
}


