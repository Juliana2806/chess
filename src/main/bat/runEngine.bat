call variables.bat


rem to turn on debug mode add -debug
rem it will create winboard debug log

%WINBOARD_PATH% -debug -cp -reuseFirst false -fcp "%JAVA_PATH% %RUN_OPTIONS% -jar %RUN_JAR_PATH%" -fd "%RUNNABLE_JAR_DIRECTORY%"