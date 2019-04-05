/* Copyright (c) 2017 dbradley. */

var s_playDemoPNG = "playdemo.png";
var s_playDemoText = "Play demo";

var s_stopDemoPNG = "stopdemo.png";
var s_stopDemoText = "Stop demo";

var s_playButt = "playbutt";

function applyGifPlayer() {
  buildGifPlayerInDivTag();
}

/**
 * Build the HTML into a <div class='gifplayer' element for a basic gif
 * player and play/stop button.
 * 
 * The source html to built into buildGifPlayerInDivTag generated source code
 * are lines: 8) - 11)
 * 
 * Line 1) links the heading to be scrolled into view with the gif player
 * 
 * line 9) is the link for the various ID attributes for the gif player
 * line 10) is a path relative to the HTML file of the gif file
 *          - the [path/][filename].gif format [filename] is key for other a stop PNG
 *          -- [path/][filename]stop.png -- file displayed when gif player is stopped
 *          
 *          - [path/] will need to contain playdemo.png and stopdemo.png for
 *             icons in the play/stop button
 * 
 */
function buildGifPlayerInDivTag() {
  //
  // source in the document would look like the following
  // 
  //  1)  <h4 id="assoctabid" class="hilitedl">Associated projects tab</h4>
  //  2)  <div class="divindent">
  //  3)   <p>
  //  4)    Associate projects work using the same table layout as the 
  //  5)    principle-project tab. The layout has sub-tabs for each associate/sub-project
  //  6)    represented.
  //  7)   </p>
  //  8)   <div class='gifplayer'
  //  9)        data-butt-img-id="assoctabid"
  // 10)        data-gifimg-path="./imgs/gif/assoctabedit.gif">
  // 11)   </div>
  // 12)  </div>
  //
  // Converted to the following HTML onload of the page
  //
  // <h4 id="assoctabid" class="hilitedl">Associated projects tab</h4>
  // <div class="divindent">
  //  <p>
  //   Associate projects work using the same table layout as the 
  //   principle-project tab. The layout has sub-tabs for each associate/sub-project
  //   represented.
  //  </p>
  //
  // <div class="gifplayer" 
  //       data-butt-img-id="assoctabid" 
  //       data-gifimg-path=".imgs/gif/assoctabedit.gif">
  //  <table class="gifplayertable">
  //   <tr>
  //    <td><img id="assoctabidgifimg" 
  //             src="./imgs/gif/assoctabeditstop.png" 
  //             alt="missing image">
  //    </td>
  //   </tr>
  //   <tr>
  //    <td>
  //     <div class="gifplaybuttdiv">
  //      <button id="assoctabidplaybutt" 
  //              onclick='playGifImgToggle("assoctabid");' 
  //              data-butt-scroll-id="assoctabid">
  //       <img src="./imgs/playdemo.png" 
  //            alt="missing image"> Play demo
  //      </button>
  //     </div>
  //     </td>
  //    </tr>
  //   </table>
  //  </div>
  // </div>

  var gifPlayerClzArr = document.getElementsByClassName("gifplayer");

  var lenClz = gifPlayerClzArr.length;
  var i;
  for (i = 0; i < lenClz; i++) {
    var gifPlayerEle = gifPlayerClzArr[i];

    // <div class='gifplayer' 
    //      data-butt-img-id="prjtabidgif"
    //      data-gifimg-path="imgs/gif/assoctabedit.gif">
    // </div>
    //
    var dataButtImgId = gifPlayerEle.getAttribute("data-butt-img-id");
    var dataGifImgPath = gifPlayerEle.getAttribute("data-gifimg-path");

    // assume GIF images will be at stop
    // '<img id='prjtabidgifimg' src="imgs/gif/assoctabeditstop.png" alt="missing image">  

    //
    // table
    // row 1 :
    // td 1 :  image that is being displayed, stopped.png or xxxxxx.gif
    // row 2:
    // td 1 : the play/stop button 
    //
    var table4GifPlayer = document.createElement("table");
    table4GifPlayer.setAttribute("class", "gifplayertable");

    var tr1GifPlayerRow = document.createElement("tr");
    var td1tr1GifPlayerTD = document.createElement("td");

    // populate with a stopped image file
    // '<img id='prjtabidgifimg' src="imgs/gif/assoctabeditstop.png" alt="missing image">  
    var td1tr1Img = document.createElement("img");

    td1tr1GifPlayerTD.appendChild(td1tr1Img);
    tr1GifPlayerRow.appendChild(td1tr1GifPlayerTD);
    table4GifPlayer.appendChild(tr1GifPlayerRow);

    // add the control play/stop button
    //
    td1tr1Img.setAttribute("id", getGifImgTagIdString(dataButtImgId));
    td1tr1Img.setAttribute("src", getStoppedGifImgPath(dataGifImgPath));
    td1tr1Img.setAttribute("alt", "missing image");

    var tr2GifPlayerRow = document.createElement("tr");
    var td1tr2GifPlayerTD = document.createElement("td");
    var divButtPlay = document.createElement("div");
    divButtPlay.setAttribute("class", "gifplaybuttdiv");

    //    <button id="prjtabidplaybutt"
    //            onclick='playGifImgToggle("prjtabid");'> 
    //      <img src="./imgs/gif/playdemo.png" alt="missing image"> 
    //      Play demo
    //     </button>

    var buttonPlayStop = document.createElement("button");
    buttonPlayStop.setAttribute("id", getGifButtonPlayId(dataButtImgId));
    buttonPlayStop.setAttribute("onclick", getGifButtonOnClickValue(dataButtImgId));
    buttonPlayStop.setAttribute("data-butt-scroll-id", dataButtImgId);

    buttonPlayStop.innerHTML = getPlayDemoPngAndText(dataButtImgId, dataGifImgPath);

    divButtPlay.appendChild(buttonPlayStop);
    td1tr2GifPlayerTD.appendChild(divButtPlay);
    tr2GifPlayerRow.appendChild(td1tr2GifPlayerTD);

    table4GifPlayer.appendChild(tr2GifPlayerRow);

    // insert into the div for gif-player
    gifPlayerEle.appendChild(table4GifPlayer);
  }
}

function getPlayDemoPngAndText(dataButtImgId, dataGifImgPath) {
  var pathToGifImagesStr = getGifImageLeadPath(dataGifImgPath) + s_playDemoPNG;

  // <img src="./imgs/gif/playdemo.png" alt="missing image">Play demo
  var imageStrAndText = "<img src=\"" + pathToGifImagesStr + "\"";
  //  imageStrAndText = imageStrAndText + " data-butt-img-id=\"" + dataButtImgId + "\"";
  imageStrAndText = imageStrAndText + " alt=\"missing image\"> " + s_playDemoText;

  return imageStrAndText;
}

function getStopDemoPngAndText(dataButtImgId, dataGifImgPath) {
  var pathToGifImagesStr = getGifImageLeadPath(dataGifImgPath) + s_stopDemoPNG;

  // <img src="./imgs/gif/playdemo.png" alt="missing image">Play demo
  var imageStrAndText = "<img src=\"" + pathToGifImagesStr + "\"";
  //  imageStrAndText = imageStrAndText + " data-butt-img-id=\"" + dataButtImgId + "\"";
  imageStrAndText = imageStrAndText + " alt=\"missing image\"> " + s_stopDemoText;

  return imageStrAndText;
}

function getGifImageLeadPath(dataGifImgPath) {
  // playdemo.png and stopdemo.png icons are co-located with
  // the GIF images files, so get the leading path part
  // "./imgs/gif/playdemo.png"
  //  *---------*
  //
  var lastSlashIdx = dataGifImgPath.lastIndexOf("/");
  var pathToGifImagesStr = dataGifImgPath.substr(0, lastSlashIdx + 1);

  return pathToGifImagesStr;
}

function getStoppedGifImgPath(dataGifImgPath) {
  var stopPngFilePath = dataGifImgPath.replace(".gif", "stop.png");

  return stopPngFilePath;
}

function getGifImgTagIdString(dataButtImgId) {
  var dataButtImgIdStr = dataButtImgId + "gifimg";

  return dataButtImgIdStr;
}

function getGifButtonPlayId(dataButtImgId) {
  var buttonPlayIdStr = dataButtImgId + s_playButt;
  return buttonPlayIdStr;
}

function getGifButtonOnClickValue(dataButtImgId) {
  // onclick='playGifImgToggle("prjtabidgifplaybutt");'
  var onClickStr = playGifImgToggle.name + "(\"" + dataButtImgId + "\");";
  return onClickStr;
}

/**
 * Toggle playing of the GIF image in a gif-player HTML layout by
 * changing a GIF for a xxxxxxxxxxxxxxstop.PNG and changing the control button
 * between play and stop appropriately. If GIF --- stop/button.
 * If xxxxxxxxxxxxxstop.PNG --- play/button.
 * 
 * [path]/[filename].gif ------ [path]/[filename]stop.png
 * 
 * [filename] is common between the image files for easy changeover
 * 
 * @param {string} gifButtonPlayId
 */
function playGifImgToggle(gifButtonPlayId) {
  //
  var playButtEle = document.getElementById(getGifButtonPlayId(gifButtonPlayId));
  var dataButtImgId = playButtEle.getAttribute("data-butt-scroll-id");

  var gifImageEle = document.getElementById(getGifImgTagIdString(gifButtonPlayId));

  // process the play-stop button to determine whether play or stop
  var srcPlayOrStopImg = playButtEle.innerHTML;
  var isPlaydemoSetIdx = srcPlayOrStopImg.indexOf(s_playDemoPNG);

  if (isPlaydemoSetIdx > -1) {
    // we have play-demo, change to stop-demo
    var playdemoText = playButtEle.innerHTML;
    var stopdemoText = playdemoText.replace(s_playDemoPNG, s_stopDemoPNG);
    stopdemoText = stopdemoText.replace(s_playDemoText, s_stopDemoText);

    playButtEle.innerHTML = stopdemoText;

    // change the src image between stop.png and .gif
    var gifImgStr = gifImageEle.getAttribute("src");

    gifImgStr = gifImgStr.replace("stop.png", ".gif");
    gifImageEle.setAttribute("src", gifImgStr);
  } else {
    // we have stop-demo, change to play-demo
    var stopdemoText = playButtEle.innerHTML;
    var playdemoText = stopdemoText.replace(s_stopDemoPNG, s_playDemoPNG);
    playdemoText = playdemoText.replace(s_stopDemoText, s_playDemoText);

    playButtEle.innerHTML = playdemoText;

    // 
    var gifImgStr = gifImageEle.getAttribute("src");

    gifImgStr = gifImgStr.replace(".gif", "stop.png");
    gifImageEle.setAttribute("src", gifImgStr);
  }
  //
  var prjtabidEle = document.getElementById(dataButtImgId);
  prjtabidEle.scrollIntoView();
}

/**
 * Goto (scroll to view) GIF player text and display according to the 
 * element with id playbuttprefix. 
 * 
 * @param {string} playbuttprefix the id of the element for the  GIF player text
 */
function gotoGifPlayerFromToc(playbuttprefix) {
  var prjtabidEle = document.getElementById(playbuttprefix);

  if (prjtabidEle !== null) {
    prjtabidEle.scrollIntoView();
  }
  // get the play/stop button prefixed by playbuttprefixand, set it focused
  var gifIdStr = playbuttprefix + s_playButt;
  var imgTableEditImgEle = document.getElementById(gifIdStr);

  if (imgTableEditImgEle !== null) {
    // set the focus to the play button and start playing (by a click action)
    imgTableEditImgEle.focus();
    imgTableEditImgEle.click();
  }
}
