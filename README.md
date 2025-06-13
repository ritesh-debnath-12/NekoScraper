# NekoScraper 🐱‍💻

> **⚠️ Best performance achieved with Firefox-based browsers (Firefox, Floorp, etc.) — Chrome compatibility is available, but things might break.**

**Blazingly fast Java-based scraper** for a very specific university’s grade portal—rewritten from my old Python scraper from first year, now turbocharged with Java and Selenium’s **`ExpectedConditions`** for speed and stability!

A great amount of effort has been put to build this tool, and it was very enjoyable. If you have any problem with the tool please email me.

`Unless you are from my university...you probably don't need this tool.`

---

## 🤷 What does it do?

Glad you asked! Just give it some information, annnd it will scrape report cards(in pdf). It also stores some data(name, registration number, SGPA, YGPA) and stores them in a csv file. It also records which students did not give the exam(hence no report card).

Why? Look at FAQ.

---

## 🔁 Flowchart
*Here is a flowchart as to how everything plays out...*
![Flowchart_goes_here](docs/images/NekoScraper.png "Easter Egg! Here is a cookie for you 🍪")

---

## 🚀 How to use

1. Head to the [**Releases**](https://github.com/ritesh-debnath-12/NekoScraper/releases/latest) page and download the latest version.
2. Rename `.env.example` to `.env`, then open it and configure:
    - Semester (e.g. `SEMESTER=1`)
    - Browser binary path
    - Driver paths (Chrome, Gecko)
    - Payload URL
    - etc, just look inside the .env you will know
3. Double-click the `.exe` in the folder. 🖱️
4. If something breaks, check the logs and send them my way(my email is at my profile)!

---

## 🛠️ How to Build from Source

Requirements:
- ✅ A Computer with Windows 10 or 11..idk might work with 7 ig? Just make sure its 64-bit
- ✅ [Git](https://git-scm.com/downloads/win)
- ✅ [Maven](https://maven.apache.org/download.cgi?.)
- ✅ [JDK 21](https://adoptium.net/temurin/releases/?os=any&arch=any&version=21)
- ✅ [IntelliJ IDEA](https://www.jetbrains.com/idea/download/?section=windows) ~~(or your favorite IDE)~~(for your and my sanity...don't use anything aside from IntelliJ)
- ✅ ~~[WiX Toolkit](https://github.com/wixtoolset/wix3/releases)~~ No longer necessary as app-image is used
- ✅ PowerShell (for Windows packaging) <- If you don't got this and you run a Windows Machine...[only this man can save you](https://en.wikipedia.org/wiki/Jesus).
- ✅ [7z](https://7-zip.org/download.html)
> **⚠️ Maven and Java should be available in the PATH. How? [Watch this](https://www.youtube.com/watch?v=pGRw1bgb1gU)...**


### **Build steps (PowerShell snippet)**:

Create a `test.ps1`(or anything, just make sure it ends with a .ps1) and paste these inside

```commandline
Set-PSDebug -Trace 1
mvn -X clean package

# Clean leftover jars
Remove-Item -Force target/original-*.jar

# Create custom JVM runtime
$modules = jdeps --multi-release 21 --print-module-deps --ignore-missing-deps target/NekoScraper.jar
jlink --output uniscraper-runtime --add-modules $modules --no-header-files --no-man-pages

# Prepare distribution folder
if (!(Test-Path dist)) { mkdir dist }

# Package into a portable app-image
jpackage --name NekoScraper --input target --main-jar NekoScraper.jar --main-class me.neko.Main --app-version "1.0" --win-console --app-content drivers --runtime-image uniscraper-runtime --type app-image --dest dist

# Copy config and docs
copy .env.example dist\
copy README.md dist\

# Zip it up
& "C:\Program Files\7-Zip\7z.exe" a "NekoScraper-1.0.7z" ".\dist\*"
```

Save and exit...double clicking test.ps1 should work, if not then run it from a terminal
```powershell
.\test.ps1
```

### Build steps **(I want to do it myself, because I like banging my head against the keyboard)**:

Alright Sherlock. Here is your step-by-step DIY 2-Minute Crafts(think that was their name):

- Git clone the repo(use git bash or other terminal...cmd is also an option)
- Open the root of the project
- Clean the target folder and make a FATASS package
```commandline
mvn -X clean package
```
- Find the dependencies required by the application
```commandline 
jdeps --multi-release 21 --print-module-deps --ignore-missing-deps target/NekoScraper.jar
```
- Copy the comma separated output(java.base, java.logging etc)
- Paste the output in the end of this command which creates a small custom runtime
```commandline 
jlink --output uniscraper-runtime --no-header-files --no-man-pages --add-modules [PASTE_HERE]
```
- Bundle it all up in a nice symphony
```commandline
jpackage --name NekoScraper --input target --main-jar NekoScraper.jar --main-class me.neko.Main --app-version "1.0" --win-console --app-content drivers --runtime-image uniscraper-runtime --type app-image --dest dist
```
- You can zip it using 7z
```commandline
C:\Program Files\7-Zip\7z.exe a "NekoScraper-1.0.7z" ".\dist\*"
```
Your zip(.7z) should be ready. Can't open it? [Use this](https://7-zip.org/download.html)

Brownie points to you if you added 7z to the path and used it like that!

> Or you can skip all this and run it using IntelliJ 🤷

---

## ❓FAQ

Q: **Why Firefox is recommended?**

A: Chrome handles downloads differently and timing can break automation (switching tabs, etc.).

Q: **Q: Can I run this on macOS or Linux?**

A: Idk, did not test it. Needs some configuration with the commands, try it! or give me a holler!

Q: **Q: Do I really need to do the .env.example to .env?**

A: Yes. How else is the machine supposed to know what you want? If you can create a Swing GUI for me, it will be appreciated!

Q: **Build fails with Java errors?**

A: This machine logs almost everything...open the log folder and give me the scraper.log(email at my profile)

Q: **Why?**

A: Because I can...and if someone wants to do some data analysis...*wink wink*

Q: **Why Java?**

A: I like Ryan Gosling

---

## 🫂 Special Thanks

I wrote 1% of the instruction to the miracle sand machine, rest 99% came from these wonderful folks.
- Selenium Team
- Log4j Team
- Java(duh)
- ChromeDriver Team
- GeckoDriver Team
- Adoptium's Java Distribution
- Oracle Docs(there are some things missing in it, but still wonderful documentation regardless!)
- im-luka for his md-cheatsheet(it looks like this courtesy to him!)
- WiX Toolkit folks(Even if the tool uses App-Image, it is waay to cool to not be mentioned)
- 7z folks
- Maven People
- Tongfei Chen's Progressbar....it makes the tool look extra cool!
- Independent Blog Writers(waaay too many to mention individually, have some mercy xD)
- Senko San's Artist(could not find who the artist is...but [here is the link to the full image](https://the-only-shoe.artstation.com/projects/1n6Ag3))