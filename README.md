# twitterMiner
A Twitter data miner using the Twitter4j library.
It records all tweets containing a keyword (Trump) to a postgres database.
Then it calculates the percentage of these keywords that contain another keyword ("impeach") and stores percentages for each minute in a separate table, in order to decrease the data size (the original tweet database is over 50GB).
It runs on a Raspberry Pi 3 so reducing the data size is critical, as a simple Hibernate request for ScrollableResults can take hours.
WIP.

Here are the current recorded historical changes (some data missing):
![As of 15 Oct, 2018](https://i.imgur.com/9nw2FX4.png)


