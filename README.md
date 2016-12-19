### Kindle Note Parser
*This repo is for personal use, not optimized to handle all cases and languages yet*

Although Kindle automatically stores clippings when reader highlights certain texts, they all exist in one txt file for all books, which makes it difficult to keep track when you are reading multiple books in parellel during a period of time.

This parser repo helps to separate highlights from different books and generate different txt files for each book note, which makes it much easier to be maintained and reviewed.


***Difference from clippings.io***
- For clippings.io, each note item need to either include all affiliated information including
 `book title, author, page, timestamp` or non at all. However, only when the note was made matters usually, so only timestamp is retained 
 in current version for each note(or highlighted) item in output.
- The highlights or notes made within 5 minutes will be aggregated as one paragraph in output files, since they are usually highly correlated, either in same section of an essay, or bullet points regarding one topic but are not consecutive in text.


***Notice***
- For timestamp of highlights, current version only support English and Chinese date formats.

###How To Use

`javac NoteProcessor.java`

`java NoteProcessor <Path for My Clippings.txt> <Directory to store separated notes>`


###Source Clippings file format
![Source Clippings file format](./src/pics/Before.png)


###Parsed Notes format
![Parsed Notes format](./src/pics/After.png)
