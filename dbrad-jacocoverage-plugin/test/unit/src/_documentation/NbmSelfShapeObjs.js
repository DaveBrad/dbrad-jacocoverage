/* Copyright (c) 2018 dbradley. All rights reserved. */

/*
 * Contains the document SVG constructs that represent various grouped
 * SVG elements in an HTML document. The constructs are shapes/symbols used
 * in documentation of the dbrad-jacocoverage projects NbmSelfTest collection
 * of test-cases.
 * 
 * The shapes are drawn on a page in a manner that is animated-like, along with 
 * explanation content so a flow of processing NbmSelfTest.
 */

/**
 * Class shape that represents a Netbeans IDE instance frame that is running.
 * The representation has a colored frame with an inner image of an IDE window.
 * 
 * @type IdeShape(SvgShapeAbstract)
 */
class IdeShape extends FramedImage {

  /**
   * Create an object which will represent a Netbeans IDE window.
   * 
   * @param {int} x coordinate position of Object
   * @param {int} y coordinate position of Object
   * @param {int} imageWidth mandatory width of image 
   * @param {int} imageHeight mandatory height of image 
   * @param {string} imageSrc path to a image file (PNG, JPEG)
   * @param {string} color of the colored frame (outer rectangle)
   * 
   * @returns {IdeObj} this
   */
  constructor(x, y, imageWidth, imageHeight, imageSrc, color) {
    super(x, y, imageWidth, imageHeight, imageSrc, color);
  }
}

/**
 * Class shape that represents a disk that is a COPY of a Netbeans
 * project structure (source, test, project settings).
 * 
 * @type CopyDiskShape(SvgShapeAbstract)
 */
class CopyDiskShape extends Cylinder {
  /**
   * Create an object which will represent a copy of a disk structure of a Netbeans
   * project.
   * 
   * @param {int} x coordinate position of Object
   * @param {int} y coordinate position of Object
   * @param {int} width of cylinder
   * @param {int} height of cylinder
   * @param {string} cylColor of the cylinder when drawn (dafault is white)
   * 
   * @returns {CopyDiskObj} this
   */
  constructor(x, y, width, height, cylColor) {
    super(x, y, width, height, cylColor, 'black', "3, 3");
    this.cylColor = cylColor;
  }
}

/**
 * Shape class that represents a disk of a Netbeans project structure 
 * (source, test, project settings).
 * 
 * @type {PermDiskShape}(SvgShapeAbstract)
 */
class PermDiskShape extends Cylinder {
  /**
   * Create an object which will represent a permanent disk structure of a Netbeans
   * project.
   * 
   * @param {int} x coordinate position of Object
   * @param {int} y coordinate position of Object
   * @param {int} width of cylinder
   * @param {int} height of cylinder
   * @param {string} cylinderColor the color-code string for the inside of the cylinder
   * @param {string} borderColor the color-code string for the cylinders border
   * 
   * @returns {PermDiskShape} this
   */
  constructor(x, y, width, height, cylinderColor = 'green',
          borderColor = 'blue', dashStroke) {
    super(x, y, width, height, cylinderColor, borderColor, dashStroke);
  }
}

/**
 * Shape class that shows a processing Icon/bar in a similar mannner as the 
 * NB IDE does when performing a task.
 * 
 * @type ProcessingIconShape
 */
class ProcessingIconShape extends StShape {
//99

  constructor(x, y, width = 100, height = 20, colorBar = 'red', colorIndicator = 'blue') {
    super(x, y, width, height);
    this.colorBar = colorBar;
    this.colorIndicator = colorIndicator;
  }

  /**
   * Internal class use.
   * 
   * Draw the shape components into an SVG group.
   * 
   * @param {SVG group element} svgGrpObj group object to place other SVG 
   *                                      components into
   */
  drawMe(svgGrpObj) {
    var x = this.xShape;
    var y = this.yShape;
    var wRect = this.widthShape;
    var hRect = this.heightShape;
    var textRectObjOuter = svgGrpObj.rect(wRect, 10)
            .move(x, y)
            .fill("red");
    // run the process indicator
    this.startIndicator();
  }

  removeMeNow() {
    stopIndicator();
    super.removeMeNow();
  }

  stopIndicator() {
    window.clearInterval(this.intervalProcess);
  }

  startIndicator() {
    var selfThis = this;
    var xBase = this.xShape + 1;
    var yBase = this.yShape + 2;
    var widthIndicator = (this.widthShape - 2) / 10;

    this.textRectObjIndicator = this.z_getSvgGroupObj().rect(widthIndicator, 6)
            .move(xBase, yBase)
            .fill('blue');
    this.countIndicator = 0;
    this.intervalProcess = window.setInterval(function () {
      selfThis._doSetIndicator(xBase, yBase, widthIndicator);
    }, 200);
  }

  _doSetIndicator(xBase, yBase, widthIndicator) {
    var adjustCount = this.countIndicator % 10;
    var nuPixelX = xBase + (widthIndicator * adjustCount);
    this.textRectObjIndicator.move(nuPixelX, yBase);
    this.countIndicator = adjustCount + 1;
  }
}

class DataLine extends StShape {

  /**
   * Create the DataLine shape.
   * 
   * @param {int} x
   * @param {int} y
   * @param {type} stroke
   * @param {type} lineColor
   * @param {type} optionalXYs
   * 
   * @returns {DataLine} this
   */
  constructor(x, y, xN, yN) {
    super(x, y, -1, -1);

    arguments.shift();

    this.polyPointsArr = new Array();

    for (var i = 0; i < arguments.length; i = i + 2) {
      var xyPairArr = new Array();

      xyPairArr[0] = arguments[i];
      xyPairArr[1] = arguments[i + 1];

      this.polyPointsArr.push(xyPairArr);
    }
  }

}
