/* Copyright (c) 2018 dbradley. */

var G_selhitliteClz = "selhitlite";

function onloadSetImageDivs() {
  var allButtimgArr = document.getElementsByClassName('buttimg');

  var len = allButtimgArr.length;
  var i;
  for (i = 0; i < len; i++) {
    var divButtimgEle = allButtimgArr[i];
    // <div tabindex="0" class="buttimg enblhi"
    //      onclick="sel(this);" 
    //      onkeypress="keysel(this, event);"></div>
    divButtimgEle.setAttribute("tabindex", "0");
    divButtimgEle.setAttribute("onclick", 'sel(this);');
    divButtimgEle.setAttribute("onkeypress", 'keysel(this, event);');
    divButtimgEle.setAttribute("onFocus", onfocusScroll.name + "(this);");
    divButtimgEle.setAttribute("onmouseover", onmouseOverOut.name + "(this, \"over\");");
    divButtimgEle.setAttribute("onmouseout", onmouseOverOut.name + "(this, \"out\");");
  }
}

/**
 * Selected element is key to an ID element which will be
 * scrolled to and into view, and have its text color changed.
 * 
 * @param {type} ele element being selected
 */
function sel(ele) {
  function removeHitliteAfter30seconds(ele) {
    var waitTime = 15000;

    var cycles = 1;
    var tt = setInterval(function () {
      if (cycles === 0) {
        clearInterval(tt);
        ele.classList.remove(G_selhitliteClz);
        return;
      }
      cycles--;
    },
            waitTime);
  }
  ;

  hilitedloff();

  var clz = ele.classList;

  var clzLen = clz.length;
  var i;

  for (i = 0; i < clzLen; i++) {
    var clzStr = clz[i];
    if (clzStr !== "buttimg") {
      var idOfSelectedHeader = clzStr + "id";

      var idEle = document.getElementById(idOfSelectedHeader);
      var elementsClasses = idEle.className;

      var alreadyPresentIdx = elementsClasses.indexOf(G_selhitliteClz);
      if (!(alreadyPresentIdx > -1)) {
        // not already set, so add it
        idEle.classList.add(G_selhitliteClz);
      }
      // scroll to the element so its displayed and then blink slowly
      // for three times
      idEle.scrollIntoView();

      // These values are safe and just enough to attract the eye to view.
      // This is done as the item of interest is within a list of other
      // items on the same page and not at the top of the page.
      var blinkSpeed = 600;
      var countLimit = 2;
      var countStart = 0;

      var stateHiliteOn = true;
      var t = setInterval(function () {
        var ele = document.getElementById(clzStr + "id");

        // apply/un-apply the class to create a blink
        if (stateHiliteOn) {
          idEle.classList.remove(G_selhitliteClz);
        } else {
          idEle.classList.add(G_selhitliteClz);
        }
        stateHiliteOn = !stateHiliteOn;

        if (countStart > countLimit) {
          clearInterval(t);
          // at the end leave the hi-light set
          idEle.classList.add(G_selhitliteClz);
          removeHitliteAfter30seconds(ele);
          return;
        }
        countStart++;
      },
              blinkSpeed);

      // the section may have a index/focusable item in its section
      // set it as the focused item for accessibility
      // 'id="prjtabidindex"' is an example
      //
      var idIdxEle = document.getElementById(idOfSelectedHeader + "index");

      if (idIdxEle !== null) {
        idIdxEle.focus();
      }
    }
  }
}
/**
 * Action to do the key press.
 * 
 * @param {type} ele element being keypress selected
 * @param {type} evt the event associated with the key press
 */
function keysel(ele, evt) {
  if (evt !== undefined) {
    var c = evt.which;
    if (c === 13 || c === 10 || c === 32) {
      sel(ele);
    }
  }
}
/**
 * Turns off the last hi-lite item for the sel/keysel functions
 */
function hilitedloff() {
  var allSetArr = document.getElementsByClassName(G_selhitliteClz);

  if (allSetArr !== null) {
    var i;
    for (i = 0; i < allSetArr.length; i++) {
      allSetArr[i].classList.remove(G_selhitliteClz);
    }
  }
}

/**
 * There is conflict between onFocus and onClick as onClick does an onFocus, so
 * to process this when a mouseOver is done on the button image DIV element
 * a value is set, and when mouseOut the value is 'reset'.
 * 
 * OnfocusScroll will use this set value to determine if the onFocus action 
 * should happen or not.
 * 
 * @param {element} ele
 * @param {string} action 'over' or 'out'
 */
function onmouseOverOut(ele, action) {
  ele.overout = action;
}

/**
 * When the buttimg div on an image item is focused upon, the item will be scrolled into
 * view. 
 * 
 * HOWEVER, if there are fixed "elements" at the bottom of the
 * page which are overlapping the scroll-able page content, the
 * scrolling would not happen. Label these with class 'bottomfixed'
 * 
 * @param {element} elebuttimg
 *
 */
function onfocusScroll(elebuttimg) {
  // resolve a conflict of onClick versus onFocus when the mouse being over
  // an elebuttimg object and a click is done then OnFocus and OnClick are both called, so
  // we don't do the focus scroll as it is a click to goto.
  if (elebuttimg.overout === "over") {
    return;
  }
  // if there are bottom-fixed elements, then the scrolling will need to calculate
  // the element into scroll-into-view scrolling. Otherwise, nothing to consider.
  var botfixedEleArr = document.getElementsByClassName("bottomfixed");

  if (botfixedEleArr.length === 0) {
    // nothing to consider
    return;
  }
  // calculate all elements that have been set as fixed at the bottom
  // of the page (outer elements that have the fixed size).
  var i;

  var fixedBottomY = -1;
  for (i = 0; i < botfixedEleArr.length; i++) {
    var ibotFixedValue = botfixedEleArr[i].offsetTop;

    if (fixedBottomY > ibotFixedValue || fixedBottomY === -1) {
      // set the least value offset, as this should include all others
      fixedBottomY = ibotFixedValue;
    }
  }
  // determine the scroll to position from the page and the offset positions
  var eleButtImgIOffset = getElementOffset(elebuttimg).top;
  var eleButtImgIHeight = elebuttimg.clientHeight;

  if (eleButtImgIOffset + eleButtImgIHeight >= fixedBottomY) {
    window.scroll(0, eleButtImgIOffset - fixedBottomY + (eleButtImgIHeight * 3));
  }
}

function getElementOffset(el) {
  const rect = el.getBoundingClientRect();

  return {
    top: rect.top + window.pageYOffset,
    left: rect.left + window.pageXOffset
  };
}