
# Interactive Inventory

## What is it?

An interactive program to view and modify inventory from webstore.com. It contains both online search and offline queries. You can add, modify, or delete an item or any of it's information: name, id, condition, bidding price, bids, buy now price, image, imgae url, timestamp, reserve price, best offer, direct payment, free shipping, and local pickup. The program can also email you the information if desired.

## To Run

From the src dirictory:
	$ javac -cp .:javax.mail.jar PhaseThree.java
	$ java -cp .:javax.mail.jar PhaseThree -i InputFile.txt -l LoggingFile.txt -c Cache.txt -o OutputFile.txt -d ../
Command Line Arguments:
* -i 'input file'
* -o 'output file'
* -d 'file path'
* -l 'logging file'
* -c 'cache file'
* -p run program without opening GUI's (will still open login gui)
* -p_email run program without GUI's (will still open login and email address input GUI) 
The input file should be a text file with each query on a seperate line.
The output, logging, and cache files are also .txt.
If you don't include file as command line arguments, file chooser GUI's will open when you run the program.
