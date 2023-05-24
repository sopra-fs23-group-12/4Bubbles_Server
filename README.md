## UZH SoPra Course FS23

<img title="favicon" alt="Soap Bubble" src="https://github.com/sopra-fs23-group-12/4Bubbles_Client/public/favicon.png" width="100">

# 4Bubble Trivia

Make Trivia fun again!  \
This is a trivia game with a focus on multiplayer aspects.
You can test your trivia knowledge by battling your friends in your preferred
topic. Choose your difficulty, the number of questions and your game mode.
You have 10 seconds to answer the questions in the bubble and collect points. The faster
you answer, the more points you will get. This will prove more difficult than you might
expect, especially in hard mode. But not to worry - since the bubble sizes change to indicate
the number of people that have already cast their vote on one of the answers, you can go
the safe road and follow your friend's choice ... or can you? \
The game supports two game modes; standard locks your choice in, once you've cast your vote
you cannot change it anymore. 3,2,1 ... let's you change your vote. Players in this game mode
have to be extra careful when deciding whether to trust their friend's choices.
Whether you chose the correct answer will be revealed when the time is up; if your
bubble doesn't burst, you've made the correct choice!

[Play here](https://sopra-fs23-group-12-client.ew.r.appspot.com). Enjoy!
Check [here](https://sopra.dkueffer.ch) whether the server is running correctly.

You can find the corresponding client repository [here](https://github.com/sopra-fs23-group-12/4Bubbles_Client).

## Technologies

This project is a Node.js application running ReactJS with JavaScript.\
Server-Client communications are handled with REST API and Socket.io with the [Netty-Socketio](https://github.com/mrniko/netty-socketio) library for the server.

## High-level Components

Our Server consists of four major components;

[GameRoom](/src/main/java/ch/uzh/ifi/hase/soprafs23/entity/GameRoom.java) and [Game](/src/main/java/ch/uzh/ifi/hase/soprafs23/game/Game.java) are the main
classes that handle the sequence of game actions and its members.
They are summarized and managed by the GameRoomController class.

Our controllers, especially [Socket](/src/main/java/ch/uzh/ifi/hase/soprafs23/controller/SocketController.java) and [GameRoom](/src/main/java/ch/uzh/ifi/hase/soprafs23/controller/GameRoomController.java) controllers handle all incoming traffic.\
SocketController specifically handles all traffic related to Websockets.

[API Service](/src/main/java/ch/uzh/ifi/hase/soprafs23/service/ApiService.java) handles the connection to the trivia question API.

The [User](/src/main/java/ch/uzh/ifi/hase/soprafs23/entity/User.java) class is the only entity we store in a [Database](/src/main/java/ch/uzh/ifi/hase/soprafs23/repository/UserRepository.java). This allows users to create
and account they can revisit and allows the system to track statistics that can be displayed to the users.

Many helper classes support the main class in their functionalities; these include the [service classes](/src/main/java/ch/uzh/ifi/hase/soprafs23/service)
for the components, the abstractions of [Votes](/src/main/java/ch/uzh/ifi/hase/soprafs23/entity/Vote.java), and manager classes, such as [RoomCoordinator](/src/main/java/ch/uzh/ifi/hase/soprafs23/entity/RoomCoordinator.java)
or [VoteController](/src/main/java/ch/uzh/ifi/hase/soprafs23/game/VoteController.java), that afford finding rooms and evaluating votes respectively.


## Launch and Deployment

### Prerequisites

All dependencies are handled with Gradle.

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


## Roadmap

Developers interested in extending this application are welcome to implement additional features.\
Here are some Ideas that can be used as inspiration:

- [Additional game modes](https://github.com/sopra-fs23-group-12/4Bubbles_Server/issues/25): We paid special attention to modularity while designing our code. It could therefore be modified to add
  some more game modes than the two that are already implemented.
- [Awards for special achievements](https://github.com/sopra-fs23-group-12/4Bubbles_Server/issues/19): User statistics are already tracked and stored. Implementing a global leaderboard and
  special awards for all players that achieve a milestone (i.e. winning 20 games) would be a nice extension.


## Authors

In alphabetical order:
* **Dario Küffer** - [GitHub](https://github.com/dariokueffer)
* **Fabio Bertschi** - [GitHub](https://github.com/fabibert)
* **Maaike van Vliet** - [GitHub](https://github.com/Bluee1Bird)
* **Marlen Kühn**  - [GitHub](https://github.com/MarlenKuehn)
* **Louis Devillers** - [GitHub](https://github.com/a1ps)


Special thanks to the authors of the template:
* **Roy Rutishauser** - [GitHub](https://github.com/royru)
* **Dennis Huber** - [GitHub](https://github.com/devnnys)


## Contributing

Please read [contributions.md](/contributions.md) for details of our task history.


## Acknowledgments

* This project was build on the basis of the [SoPra Template FS23](https://github.com/HASEL-UZH/sopra-fs23-template-server) provided by the University of Zurich for the
  Software Engineering Lab (Softwarepraktikum) course supervised by [Professor Thomas Fritz](https://www.ifi.uzh.ch/en/hasel/people/fritz.html).
* Special Thanks also to our Teaching Assistant [Valentin Hollenstein](https://github.com/v4lentin1879)for his support.
* We are also grateful to the creators and contributors of the external API [Open Trivia Database](https://opentdb.com/) which we used as source for
  our questions.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.

