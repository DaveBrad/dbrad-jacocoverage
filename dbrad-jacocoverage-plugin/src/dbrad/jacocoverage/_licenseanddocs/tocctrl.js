/* Copyright (c) 2017 dbradley. */
/**
 * Build a table-of-contents from the settings within a HTML document.
 * 
 * Requires tocctrl.css for doing the numbered h1-h6 headings and other
 * string tags items into the TOC list. In the latter case, the TOC will
 * convert and <p class="toc">,<dl><dt class="toc"><dd..., <button class="toc">,.... 
 * to be a a <p class="buttTOC">xmxmxmx where xmxmxmx will be the first 30 or
 * 40 characters of the tags text (and if > 40 then 30 characters xmxmxmx.... [periods]) 
 * 
 * NEEDS: tocctrl.css to be installed too.
 * 
 * <link rel="stylesheet" type="text/css" href="tocctrl.css">
 * <script src="tocctrl.js"></script>
 * 
 * USAGE
 * -----
 * 
 * <body onload='buildFillTOC(); ........'>
 *  <div class='bodywithtoc'>
 *   <h1 id='intro'>Introduction</p>
 *   :
 *   <h2 id='tocid'>Table of contents</h2>
 *   <div id="tocmaindiv" class="toclist">
 *   -------> place nothing in here, or at own risk
 *          <!-- { tocctrl.js will build and insert HTML between here at the DOM level 
 *                 as the TOC list } -->
 *          -------> class="toclist" all tags marked with toclist
 *                => may be placed anywhere within the div 'bodywithtoc'
 *                => with an id will be included in TOC listing  
 *                => unrestricted as to tag kind 
 *   </div>
 *   :
 *   ---- documents HTML with normal h1-h6 headings
 *   <h1 id="introid">Introduction</h1>
 *   --------> id="introid"
 *          => defines the scroll-into-view action in TOC selection to this id
 *          ===> may be overridden with 
 *               => data-tocscroll-to-id='prjtabid'
 *               => <h1 id="introid" data-tocscroll-to-id='prjtabid' ...>Introduction</h1>
 *   :
 *   ---- <p> as a TOC item 
 *   <p class='toc' data-tocscroll-to-id='prjtabid' style="display :none;">Play demo</p>
 *   --------> class='toc' 
 *          => identifies as a TOC item
 *   --------> data-tocscroll-to-id='prjtabid'
 *          => defines the scroll-into-view action in TOC selection to this id
 *   --------> style="display :none;"
 *          => means there will be a TOC item, but this tag will not appear in the
 *             readable part of the document, with the data-tocscroll-to-id 'setting'
 *             will cause the TOC click/select to go to the 'setting' id'd tag
 * <p class='toc' data-tocscroll-to-id='prjtabid' >Settings list</p>
 * <p class='toc tocindent2' data-tocscroll-to-id='prjtabid' >List item 1t</p>
 *  --------> class='toc tocindent2' 
 *          => will indent the item in the TOC listing   
 *          =====> Settings list
 *          =====>   List item 1     
 *   :
 *  </div>
 * </body>
 */

var g_TOC_hiliteTextFlashClzName = "tochilite";
var s_data_tocscroll_to_id = "data-tocscroll-to-id";
var TAB_KEY = 9;

function applyTOC() {

  buildFillTOC();
  setActionsForTocListItem();

  buildOnfocusMgr();
}

/**
 * Build the on-focus manager to act upon the whole HTML document.
 * Basically, as TAB is used to traverse the focusable elements
 * need to scroll the element into view, while dealing with any
 * fixed bottom (footer/div) elements which overlap the page and
 * the scroll-view.
 */
function buildOnfocusMgr() {
  // this is an attribute on the html so as deal with accessibility focus
  // (there should only one one of)
  var htmlEle = document.getElementsByTagName("html");

  // add to the onkeyup the onfocus manager arrangement
  var onkeyupAttr = htmlEle[0].getAttribute("onkeyup");

  var onkeystr = "onkeyupAction(event);";

  if (onkeyupAttr !== null) {
    // has something alreay
    //
    // if the original does things with the same key-caharacters, it
    // will override what is being added
    onkeystr = onkeystr + onkeyupAttr;
  }
  htmlEle[0].setAttribute("onkeyup", onkeystr);

  // add to the bottom of the body elements a text that may be focused
  // onto so the bottom of the page is shown
  var bodyEle = document.getElementsByTagName("body");
  var docEnd = document.createElement("div");
  docEnd.setAttribute("tabindex", "0");
  docEnd.setAttribute("class", "endofdoc");
  docEnd.innerHTML = "<br><br><br>";
  
  bodyEle[0].appendChild(docEnd);
}

/**
 * Applied to be executed on the html-element so as to place an item in
 * context on the view screen.
 * 
 * @param {type} evt
 */
function onkeyupAction(evt) {
  // looking for tab accessibility so as to place things into focus
  var key = evt.which;

  if (key === TAB_KEY) {
    var activeEle = document.activeElement;

    if (activeEle === null) {
      return;
    }
    onFocusMgr(activeEle);
  }
}

/**
 * Builds and alters the document DOM to add the table-of-content into
 * the document. 
 * 
 * Should be done as the first onload={function} in a document.
 */
function buildFillTOC() {
  // <h1><button class="buttTOC" data-tocscroll-to-id="introid">Project specific content</button></h1>

  // table content items will be set with call toc and as such
  // these items will be placed into the TOC

  //  var allTocArr = document.getElementsByClassName("toc");
  //  var allTocArr = document.getElementsByTagName("h*");  does not work

  var eleBodywithtoc = document.getElementsByClassName('bodywithtoc');

  var allTocArr = eleBodywithtoc[0].querySelectorAll("h1, h2, h3, h4, h5, h6, .toc");

  var lenTocArr = allTocArr.length;
  var i;

  // by finding a heading tag in this list we know its a heading
  var headTestStr = "h1h2h3h4h5h6";

  var divForTocContent = document.createElement("div");

  for (i = 0; i < lenTocArr; i++) {
    var tocItemMaybeEle = allTocArr[i];

    // the text is used in the TOC listing
    var tocText = tocItemMaybeEle.innerText;

    var idSet = tocItemMaybeEle.getAttribute("id");

    // the id can be overrridden using the data-tocscroll-to-id attribuate
    var tocScrollId = tocItemMaybeEle.getAttribute(s_data_tocscroll_to_id);
    if (tocScrollId !== null) {
      idSet = tocScrollId;
    }
    var tagOfTocItem = tocItemMaybeEle.tagName;
    tagOfTocItem = tagOfTocItem.toLowerCase();

    var idxOfTag = headTestStr.indexOf(tagOfTocItem);

    // only heading h1-h6 and p tags are allowed to be in TOC
    var headingTag = idxOfTag > -1;

    // process the tag as appropriate for h1-h6, or ELSE p
    if (headingTag) {
      // for h1-h6,
      tocItemMaybeEle.setAttribute("tabindex", "0");

      // this is a heading tag of some kind, so lets process it
      // <h1><button class="buttTOC" data-tocscroll-to-id="introid">Project specific content</button></h1>

      // will be one of:
      // 
      // <h1>Project specific content</h1>
      // <h1><button class="buttTOC" data-tocscroll-to-id="introid">Project specific content</button></h1>
      var hNtag = document.createElement(tagOfTocItem);

      // get the id, if present
      if (idSet === null) {
        // include in TOC but not as a selectable item
        // <h1>Project specific content</h1>
        hNtag.innerHTML = tocText;
      } else {
        // set the butt class to buttTOC and if there is a tocindent[n] class
        // add it too
        var classToSet = "buttTOC";
        // include in TOC as a selectable button heading
        // <h1><button class="buttTOC" data-tocscroll-to-id="introid">Project specific content</button></h1>
        var buttonTag = document.createElement('button');
        buttonTag.setAttribute("class", classToSet);
        buttonTag.setAttribute(s_data_tocscroll_to_id, idSet);

        buttonTag.innerHTML = tocText;

        hNtag.appendChild(buttonTag);
      }
      //
      divForTocContent.appendChild(hNtag);
    } else {
      // is an item that has been stated as a TOC, but none heading
      //
      // only the first 30 characters will be considered for the TOC listing, otherwise
      // could have <p> elements fully appearing in the TOC (oops)
      //
      var lenTocText = tocText.length;

      // restrict the characters length to 30 characters, but if the
      // less then 40 take all (this is because  xxxx 30 xxxx....
      // the 30 will have 4 chars added at end
      var lenTextTest1 = 30;
      var lenTextTest2 = 40;

      if (lenTocText > lenTextTest1) {
        // between 30 and 40 use the complete set, but bigger  then 40 restrict to
        // 30
        if (lenTocText > lenTextTest2) {
          tocText = tocText.substring(0, lenTextTest1) + "....";
        }
      }
      // will be one of:
      // 
      // <p>Project specific content</p>
      // <p><button class="buttTOC" data-tocscroll-to-id="introid">Project specific content</button></p>
      var ptag = document.createElement('p');

      // get the id, if present
      if (idSet === null) {
        // include in TOC but not as a selectable item
        // <p>Project specific content</p>

        ptag.innerHTML = tocText;

      } else {
        // include in TOC as a selectable button heading
        // <p><button class="buttTOC" data-tocscroll-to-id="introid">Project specific content</button></p>
        var buttonTag = document.createElement('button');
        buttonTag.setAttribute("class", "buttTOC");
        buttonTag.setAttribute(s_data_tocscroll_to_id, idSet);

        buttonTag.innerHTML = tocText;

        ptag.appendChild(buttonTag);
      }
      divForTocContent.appendChild(ptag);

      var currClzz = tocItemMaybeEle.getAttribute("class");
      var clzzArr = currClzz.split(" ");
      if (clzzArr !== null) {
        var j;

        var tocindentFound = "";
        for (j = 0; j < clzzArr.length; j++) {
          var idx = clzzArr[j].indexOf("tocindent");
          if (idx === 0) {
            tocindentFound = clzzArr[i];
          }
        }
        var nuClzz = currClzz + " " + tocindentFound;
        ptag.setAttribute("class", nuClzz);
      }

    }
  }
  var tocMainDivEle = document.getElementById("tocmaindiv");
  tocMainDivEle.appendChild(divForTocContent);
}

/**
 * Set the actions for all the TOC list items that have been previously 
 * set/determined. This includes different actions for the list item
 * which be made a pseudo-button.
 * 1) onClick will be a select to jump to the section in question.
 * 1a) normal heading or free-standing TOC item in document
 * 1b) a gif-player item to goto and scroll the player div
 *     into view.
 * 2) onFocus will be accessibility to ensure the list item is
 *    scrolled into view 
 */
function setActionsForTocListItem() {
  var allButtTOCArr = document.getElementsByClassName("buttTOC");

  // some buttons will be buttTOC and meed to have a special
  // onclick calling and onfocus control

  var lenI = allButtTOCArr.length;
  var i = 0;

  for (i = 0; i < lenI; i++) {
    var iEle = allButtTOCArr[i];

    var dataPlayGifAttr = iEle.getAttribute("data-play-gif");

    // original design was to apply the onfocusToc to each element,
    // however this did not deal with other none-TOC elements for
    // the focus needs
    // // the TOC list item needs to be scrolled into view when being
    // // traversed into by the tab accessibility UI
    // iEle.setAttribute("onFocus", "onfocusToc(this)");

    if (dataPlayGifAttr === null) {
      // this will be a simple position to a TOC heading location in the
      // document
      //
      // <button class="buttTOC" s_toc_scroll_idd="prjtabid"><i>{project}</i> tab</button>
      // 
      iEle.setAttribute("onClick", selTOC.name + "(this);");
    } else {
      // <button class='buttTOC' 
      //         data-tocscroll-to-id="assoctabid"
      //         data-play-gif= "imgs/gif/assoctabedit.gif"
      //  >play demo</button>
      //
      // onclick="gotoGifPlayerFromToc('assoctabid');">
      //
      var dataIdAttr = iEle.getAttribute(s_data_tocscroll_to_id);

      var actionStr = "gotoGifPlayerFromToc('" + dataIdAttr + "');";
      iEle.setAttribute("onClick", actionStr);
    }
  }
}

/**
 * When the TOC item is focused upon it is scrolled into view and
 * depends on the following requirement. 
 * 
 * DEPENDS: fixed "elements" at the bottom of the
 * page which overlap the scroll-able page content, need to
 * be set with class='bottomfixed' so the scroll position may be 
 * calculated correctly.
 * 
 * @param {element} eleTocListItem
 */
function onFocusMgr(eleTocListItem) {
  // position: fixed; items at the bottom overlap/overlay the scroll
  // area of a page and as such need to be managed around so onFocus
  // scroll-into-view take the fixed elements into consideration.
  var botfixedEleArr = document.getElementsByClassName("bottomfixed");

  if (botfixedEleArr.length === 0) {
    return;
  }
  // calculate all elements that have been set as fixed at the bottom
  // of the page. If there are a number of, then the fixed element
  // that is highest within the page (has the lowest top-offset) will
  // be used.
  var i;

  var fixedBottomY = -1;
  for (i = 0; i < botfixedEleArr.length; i++) {
    var ibotFixedValue = botfixedEleArr[i].offsetTop;

    if (fixedBottomY > ibotFixedValue || fixedBottomY === -1) {
      // set the least value offset, as this should include all others
      fixedBottomY = ibotFixedValue;
    }
  }
  // determine the scroll position of page and offset positions
  var eleTocY = getElementOffset(eleTocListItem).top;
  var tocTextHeight = eleTocListItem.clientHeight;

  //99 this will happen for <a>
  if (tocTextHeight === 0) {
    tocTextHeight = eleTocListItem.offsetHeight;
  }

  if ((eleTocY + tocTextHeight) >= (fixedBottomY - (tocTextHeight * 3))) {
    window.scroll(0, eleTocY - fixedBottomY + (tocTextHeight * 3));
  }
}
/**
 * Selected element is keyed to an ID element which will be
 * scrolled-into-view, and have its text color changed.
 * 
 * @param {element} ele element being selected
 */
function selTOC(ele) {

  function removeTocHitliteAfter30seconds(ele) {
    var waitTime = 15000;

    var cycles = 1;
    var tt = setInterval(function () {
      if (cycles === 0) {
        clearInterval(tt);
        ele.classList.remove(g_TOC_hiliteTextFlashClzName);
        return;
      }
      cycles--;
    },
            waitTime);
  }
  ;

  hilitedloffTOC();

  var dataIdAttr = ele.getAttribute(s_data_tocscroll_to_id);

  if (dataIdAttr !== null) {
    var idOfSelectedHeader = dataIdAttr;

    var idEle = document.getElementById(idOfSelectedHeader);
    idEle.classList.add(g_TOC_hiliteTextFlashClzName);

    // scroll to the element so its displayed and then blink slowly
    // for three times
    idEle.scrollIntoView();

    // These values are safe and just enough to attract the eye to view.
    // This is done as the item of interest is within a list of other
    // items on the same page and not at the top of the page.
    var blinkSpeed = 600;
    var countLimit = 1;
    var countStart = 0;
    var t = setInterval(function () {
      var ele = document.getElementById(idOfSelectedHeader);
      ele.classList.add(g_TOC_hiliteTextFlashClzName);

      if (countStart > countLimit) {
        clearInterval(t);
        ele.classList.add(g_TOC_hiliteTextFlashClzName);
        removeTocHitliteAfter30seconds(ele);
        return;
      }
      countStart++;
    },
            blinkSpeed);

    // the section may have a play button item in its section
    // set it as the focused item for accessibility
    // 'id="prjtabidindex"' is an example
    //
    //99
    var idIdxEle = document.getElementById(idOfSelectedHeader + s_playButt);

    if (idIdxEle !== null) {
      idIdxEle.focus();
    }
  }
}

/** Turns all TOC hi-lite items off. */
function hilitedloffTOC() {
  var hilitedlArr = document.getElementsByClassName(g_TOC_hiliteTextFlashClzName);

  var i;
  for (i = 0; i < hilitedlArr.length; i++) {
    var itemEle = hilitedlArr[i];
    itemEle.classList.remove(g_TOC_hiliteTextFlashClzName);
  }
}

function getElementOffset(el) {
  const rect = el.getBoundingClientRect();

  return {
    top: rect.top + window.pageYOffset,
    left: rect.left + window.pageXOffset
  };
}
