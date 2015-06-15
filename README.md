# QuandlDownloader
Do you have a subscription to Quandl premium data and always want the freshest data but don't want the unreliablity of a cron job
and the hassles of unzip code?

This tool checks every minute for your list of subscription databases.  If you subscribe to a new database, this automaticall
accounts for that.  If any database on your list has been found to be updated since your last download, it will automatically
download that database and unzip the contents.  The process then goes to sleep and wakes up a minute later repeating the checks.

## Instructions
1) rename the "settings-sample.yml" file to "settings.yml" and change the string YOUR_AUTHENTICATION_TOKEN to your token.
2) Launch the app.  If from the command line, use the command:  java -jar QuandlDownloader.jar

## FAQ
Q: Why have a "settings-sample.yml" file?
A: This is so that we all (you included) can test active auth tokens in settings.yml and have that file be in .gitignore.

Q: This app runs all day?  What resources does it use?
A: Almost none while it's sleeping.  When it's checking for updates, it is only two HTTPS calls, one only retrievs header information.
