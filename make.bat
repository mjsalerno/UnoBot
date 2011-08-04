@echo off
mkdir src\lib
copy pircbot-1.5.0\pircbot.jar src\lib\
cd src
javac -cp lib\pircbot.jar uno2\*.java
echo Class-Path: lib/pircbot.jar > man.txt
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
copy pircbot-1.5.0\pircbot.jar lib\
pause

