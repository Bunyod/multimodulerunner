package testapp

import akka.actor.Actor

// (1) changed the constructor here
class HelloActor(myName: String) extends Actor {
    def receive = {
        // (2) changed these println statements
        case "hello" => println("hello from %s".format(myName))
        case _       => println("'huh?', said %s".format(myName))
    }
}