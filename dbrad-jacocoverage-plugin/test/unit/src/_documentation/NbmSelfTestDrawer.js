/* Copyright (c) 2018 dbradley. All rights reserved. */

function tellStorystory1(svgjsObj) {
  var draw = prepareFromSVGJS(svgjsObj);
  var storyDrawer1 = new StoryDrawer(draw, 'story1', 1000, 420);

  // principle colors for the various component story stages
  var sysStorageColor = 'white';
  var sysTempStorageColor = '#f6f9f9';

  // javascript does not have easy access to CSS
  var devIdeColor = '#00b300';
  var testbedIdeColor = '#F5A9F2';
  var testingIdeColor = '#08ebfa';
  var resultsColor = '#daff33';

  // - - - - - - dev-IDE and disk - - - - - - - - - - - 
  var devIde = new IdeShape(0, 30, 420, 227, './nbmselftest30perc.png', devIdeColor);

  var devDisk = new PermDiskShape(530, devIde.y(), 80, 100, sysStorageColor);
  var devInrDisk = new CylinderInner(devDisk, 15, 24, devIdeColor).applyInner();

  var devDiskText = new TextBox(devInrDisk.xw() + 3, devInrDisk.y())
          .setText("Developer-IDE git storage")
          .borderOff();

  devIde.addTogetherObj(devDisk);
  devIde.addTogetherObj(devDiskText);

  storyDrawer1.addNext(
          devIde.fadeInTran(3, 10), 'draw the dev-IDE fade-in',
          new PauseTran(2));

  var devideText = new TextTalkAutom(devIde.x(), devIde.yh())
          .addTimeText(20,
                  0, "Stage 1a",
                  "The developer-IDE is the launching point of the self-test.",
                  2, "A user selects a self-test test-case to 'test File w/jacocoverage'.",
                  4, " ",
                  6, "Stage 1b",
                  "Project NBM and source-code is copied to temp-storage."
                  );

  var devideTeleText = new TeleTextAutom(devDisk.xw() + 30, devDisk.h() + 50, 45)
          .changeFontSize(16)
          .addText("The self-test will copy the dbrad-jacocoverage source code ",
                  "to temporary storage.")
          .setSpeed(0.2);

  storyDrawer1.addNext(
          devideText,
          devideTeleText);

  //- - - - - -  testbed disk and IDE- - - - - - - - - 
  storyDrawer1.addNext(new PauseTran(12));

  var testbedDisk = new PermDiskShape(devDisk.x() + 30, devDisk.h() + 60, 80, 80,
          sysTempStorageColor, 'blue', '5,5,5');

  var testbedInrDiskHeight = 6;
  var testbedInrDisk = new CylinderInner(testbedDisk, 12, testbedInrDiskHeight,
          testbedIdeColor, undefined, '2,2');

  var testbedDiskText = new TextBox(testbedInrDisk.xw() + 3, testbedInrDisk.y())
          .setText("Test-bed-IDE temp storage (copy of source)")
          .borderOff();

  testbedDisk.addTogetherObj(testbedDiskText);
  //
  storyDrawer1.addNext(
          new PauseTran(2),
          testbedDisk, 'the testbed-disk is created and a copy created',
          new PauseTran(1));

  // - - - - - create the testbed-disk before the IDE presents - - - - - - - - - - - - 
  var ii;
  var copyDiskN;
  var nuDiskHeight = testbedInrDiskHeight;

  for (ii = 0; ii < 4; ii++) {
    copyDiskN = new CopyDiskShape(devDisk.xCenter(60), devDisk.yCenter(),
            60, 12,
            testbedIdeColor);

    var moveTran = copyDiskN.moveToTrailTran(
            testbedInrDisk.xCenter(60),
            testbedInrDisk.yCenter(),
            5, 0.15);

    moveTran.setRemoveAfterTransition(0.1);

    var copySrcTransition;
    if (ii === 0) {
      copySrcTransition = testbedInrDisk.applyInnerTran();
    } else {
      nuDiskHeight += testbedInrDiskHeight;
      copySrcTransition = testbedInrDisk.adjustHeightTran(nuDiskHeight);
    }
    storyDrawer1.addNext(
            copyDiskN, 'copy disk shape created',
            moveTran, 'move transition shows a copy action taking place',
            copySrcTransition, 'the testbed-IDE storage increases on each copy');
  }
  // --- testbed-IDE creation which happens after the copy
  var testbedIde = new IdeShape(devIde.x() + 30, devIde.y() + 30, 420, 227,
          './nbmselftest_manual_noloop.gif', testbedIdeColor);

  var testbedideText = new TextTalkPresenter(testbedIde.x(), testbedIde.yh())
          .addTimeText(30,
                  0, 'Stage 2',
                  'The testbed-IDE loads dbrad-jacocoverage projects from temporary storage,',
                  1, "and builds the projects.", " ",
                  15, 'The project is coverage setup for all source folders.', " ",
                  -7, "A test-case is selected and 'Test File w/jacocoverage' invoked."
                  );

  storyDrawer1.addNext(
          testbedIde, 'the testbed-IDE GIF runs for about 24 seconds',
          testbedideText);
          
  // - - - testing IDE, which is test IDE launched by the testbed IDE - - - 
  var testingIde = new IdeShape(testbedIde.x() + 30, testbedIde.y() + 30, 420, 227,
          './nbmselftest_tstngide_4.gif', testingIdeColor);

  var testingideText = new TextTalkAutom(testingIde.x(), testingIde.yh())
          .addTimeText(20,
                  0, 'Stage 3',
                  'testing-IDE performs test-case, as driven by testbed-IDE.',
                  14, ' ', 'Once testing completes, backout starts.');
                  
  // - - - backout as the testing completes
  var testbedideText2 = new TextTalkAutom(testbedIde.x(), testbedIde.yh())
          .addTimeText(15,
                  0, 'Stage 3 - backout',
                  'On test completion the testbed-IDE will build the coverage results,',
                  ' ',
                  5, "and results are copied to '$HOME/nbmselftest/rpts'."
                  )
          .setDoNotRemoveAfterTime();

  storyDrawer1.addNext(
          testingIde, 'testing-IDE is a GIF which takes ~24 seconds to display',
          new PauseTran(5),
          testingideText,
          new PauseTran(24 - 5),
          testingIde.fadeOutTran(1.5, 30), 'testing finishes and the testing-IDE is taken down',
          testbedideText2,
          new PauseTran(2));
  // - - - - 
  testbedIde.addRemoveTogetherObj(testbedideText2);

  //- - - - - store the results from the test-bed onto some storage - - - 
  var copyActionReports = new CopyDiskShape(testbedIde.x() + 100, testbedIde.y() + 60,
          40, 8, resultsColor);

  var moveTranRpt = copyActionReports.moveToTrailTran(
          devDisk.x() + 20, devDisk.y() + 75, 10, 0.1);
  moveTranRpt.setRemoveAfterTransition(0.3);

  var innerResultsInrDisk = new CylinderInner(devDisk, 60, 8, resultsColor, 'black');

  var innerResultsInrDiskRslt = new TextBox(devInrDisk.xw() + 3, innerResultsInrDisk.y())
          .setText("test results: $HOME/nbmselftest/rpts/")
          .borderOff();

  // write story
  storyDrawer1.addNext(
          copyActionReports, 'copy action disk is created',
          moveTranRpt, 'copy action to report storage move transition',
          innerResultsInrDisk.applyInnerTran(), 'apply yhe report inner cylinder on the storage',
          innerResultsInrDiskRslt, 'show text about the report storage location',
          new PauseTran(2));

  testbedIde.addRemoveTogetherObj(testbedDisk);
  storyDrawer1.addNext(
          new PauseTran(5),
          testbedIde.fadeOutTran(1.5, 30), 'fade-out the testbed-IDE & its disk');

  var closingText = new TextTalkAutom(devDisk.x(), devDisk.yh(), -1, -1,
          'black', resultsColor)
          .addTimeText(30,
                  0, 'Completion',
                  'The user may access the test results folder to look ',
                  'at results using a browser.',
                  ' '
                  )
          .setDoNotRemoveAfterTime();

  storyDrawer1.addNext(closingText);

  storyDrawer1.tellStory();
}
