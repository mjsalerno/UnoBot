@echo off
mkdir src\lib
copy pkgs\pircbotx-1.9.jar src\lib\
cd src
javac -cp lib\pircbotx-1.9.jar uno2\*.java
echo Class-Path: lib\pircbotx-1.9.jar > man.txt
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
copy pkgs\pircbotx-1.9.jar lib\