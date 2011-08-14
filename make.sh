mkdir src/lib/
cp pkgs/PircBotMod/dist/PircBotMod.jar src/lib/
cd src
javac -cp lib/PircBotMod.jar uno2/*.java
echo Class-Path: lib/PircBotMod.jar > man.txt
echo Main-Class: uno2.unoBotMain >> man.txt
jar cfm UnoBot.jar man.txt uno2 lib
rm man.txt
rm -r lib
cd uno2
rm *.class
cd ../..
cp src/UnoBot.jar .
rm src/UnoBot.jar
mkdir lib
cp pkgs/PircBotMod/dist/PircBotMod.jar lib/