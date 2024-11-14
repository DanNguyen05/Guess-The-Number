@echo off
mkdir bin 2>nul

echo Compiling...
javac -d bin -cp "lib/*" src/*.java

echo Running...
java -cp "bin;lib/*" Main

pause 