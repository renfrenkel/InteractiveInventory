This is phase 3 of the project.  It is a program to store and modify an inventory from webstore.com.
To compile and run the program:
    cd into the PhaseThree directory and then into the src directory
	$ javac -cp .:javax.mail.jar PhaseThree.java
	$ java -cp .:javax.mail.jar PhaseThree -i InputFile.txt -l LoggingFile.txt -c Cache.txt -o OutputFile.txt -d ../
		flags:
			-i 'input file'
			-o 'output file'
			-d 'file path'
			-l 'logging file'
			-c 'cache file'
			-p run program without opening GUI's (will still open login gui)
			-p_emaile run program without GUI's (will still open login and email address input GUI) 
The input file should be a text file with each query on a seperate line.
The output, logging, and cache files are also .txt.
If you don't include file as command line arguments, file chooser GUI's will open when you run the program.