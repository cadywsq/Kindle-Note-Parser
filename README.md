### Kindle Note Parser
*This repo is for personal use, not optimized to handle all cases and languages yet*

Although Kindle automatically generates clippings when readers highlight certain texts and stores them, they all 
exists in one txt file for all books, which makes it difficult to keep track when you are reading multiple books 
during a period of date.

This parser helps to divide highlights from different books into different txt files, which makes it easy to be 
maintained and reviewed.

An adaptive functionality(12/10/2016):
The highlights or notes made within 5 minutes will be aggregated as one paragraph in output files.

####Usage
`javac FileProcessor.java`

`java FileProcessor <My Clippings.txt Path> <Folder for separated notes>`