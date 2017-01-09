SET WINBOARD_INSTALLATION_PATH=E:\Games\WinBoard-4.7.3
rem UI that we use to run our Chess with
SET WINBOARD_PATH=%WINBOARD_INSTALLATION_PATH%\WinBoard\winboard.exe
rem I use the Winboard installation as a Chess deployment target
rem it should be equal to 'project.deployDirectory' property in pom.xml
SET RUNNABLE_JAR_DIRECTORY=%WINBOARD_INSTALLATION_PATH%\LeokomChess
SET JAVA_PATH=D:\Java\jdk1.8.0_77\bin\java.exe
SET RUN_JAR_PATH=%RUNNABLE_JAR_DIRECTORY%\Chess.jar
SET RUN_OPTIONS=-Dblack=Legal

SET ENGINE=%JAVA_PATH% %RUN_OPTIONS% -jar %RUN_JAR_PATH%