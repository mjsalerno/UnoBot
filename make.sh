mkdir src/lib/
cp pkgs/pircbot-1.5.0/pircbot-1.5.0-ssl.jar src/lib/
cd src
javac -cp lib/pircbot-1.5.0-ssl.jar uno2/*.java
echo Class-Path: lib/pircbot-1.5.0-ssl.jar > man.txt
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
cp pkgs/pircbot-1.5.0/pircbot-1.5.0-ssl.jar lib/