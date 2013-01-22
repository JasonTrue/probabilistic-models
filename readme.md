#Project Details

The current purpose of this project is to experiment with probabilistic models in Scala. If you find it useful,
great. At this point, there's no attempt to be production quality, and I expect to make plenty of errors on the way as
I learn about both Scala and about Bayesian networks, Markov models and other probabilistic graphical models.

#Stability

It isn't. I reserve the right to completely undermine, blow away, or otherwise break any implementation or interface
in this project because at this phase of the project I have no customers depending on it, and I expect I'll learn
along the way that I've made some terrible decisions that I'll want to be able to back out of. There's also a pretty
good chance the project name will change at some point, since it's far from being a catchy name right now.

Additionally, because I don't currently have a delivery timeline for the project, when I need to make a choice to take
on a particular dependency, I'm likely to do the opposite of what I'd do for software meant for near-term production
deployment. When evaluating libraries, instead of automatically choosing one I've used before and have some familiarity
with, I'm likely to pick one that seems like a more interesting approach that gels with my particular development
preferences and biases, even if it means a steeper learning curve and higher risk.

This project was started with the amusingly improbable goal of doing it in my spare time, days after my wife and I
had our second child. If you're a parent, you already understand, but if you've never taken care of a newborn,
expect fairly sluggish progress, since I'm both busy and sleep-deprived at the moment.

#Coding standards

For the most part I try to apply whatever the common conventions are for a particular language, and I'm spend enough
time in Ruby, C#, Java, and a few other languages that I'm used to switching back and forth.

I am not particularly familiar with Scala community conventions. I am reasonably familiar with Java coding conventions,
and I have experience with hybrid functional languages in the OCAML tradition, including F#, so chances are that
I'll probably 1) Learn by available examples in Scala and 2) Leverage my habits from Java and F# where there's any
ambiguity, until I have enough experience, and probably enough of an audience, to motivate myself to reliably apply
Scala conventions.

#Building

Because I don't have a pre-existing customer or existing build infrastructure to support, I mostly arbitrarily chose
Gradle for the purpose of managing dependencies and building the project. I'm familiar with Maven, and have
been using Groovy in another couple of projects recently, and I like terse, expressive languages that eliminate
unnecessary boilerplate. So Gradle fits with those preferences nicely without losing the dependency management features
that Maven offers.

Until I have any reason to do anything fancier, the build steps are:

1. Make sure you have Gradle 1.3 or higher installed and configured.
2. Run `gradle build`

There's a chance you may need to have Ant on your path to run tests.

#Running

At this point, I'm only targeting library-like use of this project, and there's no application to speak of.

The only meaningful documentation at this point are tests.

#License

Copyright 2013 Jason Truesdell and YuzuTen LLC, a Washington State Limited Liability Company.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
