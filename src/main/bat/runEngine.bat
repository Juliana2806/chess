SET WINBOARD_PATH=E:\Games\WinBoard-4.7.3\WinBoard\winboard.exe
rem this directory must be synchronous with jar destination
rem I use the Winboard installation sub-dir for simplicity
SET RUNNABLE_JAR_DIRECTORY=E:\Games\WinBoard-4.7.3\LeokomChess
SET JAVA_PATH=D:\Java\jdk1.8.0_77\bin\java.exe
SET RUN_JAR_PATH=%RUNNABLE_JAR_DIRECTORY%\Chess.jar
SET RUN_OPTIONS=-Dblack=Legal

rem to turn on debug mode add -debug
rem it will create winboard debug log

%WINBOARD_PATH% -debug -cp -reuseFirst false -fcp "%JAVA_PATH% %RUN_OPTIONS% -jar %RUN_JAR_PATH%" -fd "%RUNNABLE_JAR_DIRECTORY%"