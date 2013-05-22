export BASE_DIR=$BMT_WS/BMT/output/release
cp -rf $BMT_WS/BMT/ants/test $BASE_DIR/ants/
ant BMTTestGUI -f $BASE_DIR/ants/test/regressGUI.xml \
-Declipse.home=$ECLIPSE_HOME \
-Dtests.jar=$BASE_DIR/tests.jar \
-Dsrc.jar=$BASE_DIR/BMT_v0.6.jar \
-Dbasedir=$BASE_DIR