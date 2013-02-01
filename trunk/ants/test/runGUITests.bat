:: remove bin dir to prevent BMT from launching from it
rmdir %BMT_WS%\BMT\bin /S /Q
ant BMTTestGUI -f %BMT_WS%\BMT\ants\test\regressGUI.xml ^
-Dbinloctmp.tests=%tests_bin% ^
-Ddir.bin=%tests_bin% ^
-Dbinloctmp=%BMT_WS%\BMT\output\release\*.jar ^
-Declipse.home=%ECLIPSE_HOME% ^
-Dbasedir=%BMT_WS%\BMT