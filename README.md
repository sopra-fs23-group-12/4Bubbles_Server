# UZH SoPra Course FS23 

# 4Bubble Trivia

Make Trivia fun again!  \
This is a trivia game with a focus on multiplayer aspects. 
You can test your trivia knowledge by battling your friends in your preferred
topic. Choose your difficulty, the number of questions and your game mode. 
You have 10 seconds to answer the questions in the bubble and collect points. The faster
you answer, the more points you will get. This will prove more difficult than you might
expect, especially in hard mode. But not to worry - since the bubble sizes change to indicate
the number of people that have already cast their vote on one of the answers, you can go
the safe road and follow your friend's choice ... or can you? Depending on your gamemode,
bubble sizes could just be randomly generated without you knowing, or players could change
their answer in the last seconds to try and confuse you. 
Whether you chose the correct answer will be revealed when the time is up; if your 
bubble doesn't burst, you've made the correct choice!

[Play here](sopra-fs23-group-12-client.ew.r.appspot.com). Enjoy!



## Technologies

This project is a Node.js application running ReactJS with JavaScript.\
Server-Client communications are handled with REST API and Socket.io with the [Netty-Socketio](https://github.com/mrniko/netty-socketio) library for the java server.

## High-level Components

*what is meant by this?? server, client and DB?

## Contributing

Please read [contributions.md]() for details of our task history.

## Launch and Deployment


### Setup this Template with your IDE of choice
Download your IDE of choice (e.g., [IntelliJ](https://www.jetbrains.com/idea/download/), [Visual Studio Code](https://code.visualstudio.com/), or [Eclipse](http://www.eclipse.org/downloads/)). Make sure Java 17 is installed on your system (for Windows, please make sure your `JAVA_HOME` environment variable is set to the correct version of Java). \
Clone the repo and open in your IDE of choice. Note: you might have to build gradle first.

### Build

```bash
./gradlew build
```

### Run

```bash
./gradlew bootRun
```

You can verify that the server is running by visiting `localhost:8080` in your browser.

### Test

```bash
./gradlew test
```

### Prerequisites

TODO: are there any prerequisites? I dont think so since it is in google cloud right?<
OR do we actually have to provide info on how to run it locally?


## Illustrations

Login/signup
welcomepage
user stats
create a game
join a game
questions
ranking

## Roadmap
tbd





## Authors

in alpahbetical order:
* **Dario Küffer** - [GitHub](https://github.com/dariokueffer)
* **Fabio Bertschi** - [GitHub](https://github.com/fabibert)
* **Maaike van Vliet** - [GitHub](https://github.com/Bluee1Bird)
* **Marlen Kühn**  - [GitHub](https://github.com/MarlenKuehn)
* **Louis Devillers** - [GitHub](https://github.com/a1ps)


Special thanks to the authors of the template:
* **Roy Rutishauser** - [GitHub](https://github.com/royru)
* **Dennis Huber** - [GitHub](https://github.com/devnnys)

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

* This project was build on the basis of the SoPra Template FS23 provided by the University of Zurich for the 
Software Engineering Lab (Softwarepraktikum) course supervised by Professor Thomas Fritz
* Special Thanks also to our Teaching Assistant [Valentin Hollenstein](https://github.com/v4lentin1879)

