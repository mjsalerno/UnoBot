mkdir src/lib/
cp pircbot-1.5.0/pircbot.jar src/lib/
cd src
javac -cp lib/pircbot.jar uno2/*.java
echo Class-Path: lib/pircbot.jar > man.txt
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
cp pircbot-1.5.0/pircbot.jar lib/