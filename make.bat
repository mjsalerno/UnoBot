@echo off
mkdir src\lib
copy pkgs\pircbot-1.5.0\pircbot-1.5.0-ssl.jar src\lib\
cd src
javac -cp lib\pircbot-1.5.0-ssl.jar uno2\*.java
echo Class-Path: lib/pircbot-1.5.0-ssl.jar > man.txt
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
copy pkgs\pircbot-1.5.0\pircbot-1.5.0-ssl.jar lib\
