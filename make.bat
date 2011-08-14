@echo off
mkdir src\lib
copy pkgs\PircBotMod\dist\PircBotMod.jar src\lib\
cd src
javac -cp lib\PircBotMod.jar uno2\*.java
echo Class-Path: lib/PircBotMod.jar > man.txt
echo Main-Class: uno2.unoBotMain >> man.txt
jar cfm UnoBot.jar man.txt uno2 lib
del man.txt
rd /s /q lib
cd uno2
del *.class
cd ..\..
copy src\UnoBot.jar .
del src\UnoBot.jar
mkdir lib
copy pkgs\PircBotMod\dist\PircBotMod.jar lib\
pause

