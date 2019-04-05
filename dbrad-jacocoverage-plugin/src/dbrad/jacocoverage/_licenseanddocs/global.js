/* Copyright (c) 2017 dbradley. */

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
  };
  
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
 * Turns off the last hi-lite item for the sel function
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


