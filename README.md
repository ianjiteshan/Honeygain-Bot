# Honeygain Bot
Honeygain Bot opens the honey jar for you without even going on their website. You can login via your access token or your email and password. You can configure the bot to open the honey jar every 24 hours or just once before it shuts itself down. You can set this up on a server and leave it running for as long as you want, I will be running this on a Raspberry PI.

## Access Token
An access token is a string of letters that is used to perform actions on your account. To get your access token, open developer tools (or inspect element) on a chromium based browser and go to the console tab. Type in `localStorage.getItem('JWT')` into the console and press enter. The console with print your access token.

## Installation

If you want to login with your email and password, edit the run_login.bat file. Enter your email at the end of `set email=` and enter your password at the end of `set password=`. Now you can double click the run_login.bat file.

If you want to login with your access token, edit the run_token.bat file. Simply enter your access token (with or without the quotation marks on either end) at the end of `set access_token=`. Now you can double click the run_token.bat file.

If you don't want to use the .bat files, you can manually execute the bot with terminal/cmd:

    java -jar HoneygainBot.jar [Access Token] [Repeat after 24 hours? True/False]

 For email and password:

    java -jar HoneygainBot.jar [Email] [Password] [Repeat after 24 hours? True/False]

## Warning
I am unsure whether this is breaking TOS or not. I have sent the staff a message, and I'll edit this as soon as I find out.