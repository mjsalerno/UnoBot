mkdir src/lib/
cp pkgs/pircbotx-1.9.jar src/lib/
cd src
javac -cp lib/pircbotx-1.9.jar uno2/*.java
echo Class-Path: lib/pircbotx-1.9.jar > man.txt
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
cp pkgs//pircbotx-1.9.jar lib/
