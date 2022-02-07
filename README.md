# vehicleapp

Hi,

the main challenge of writing the coding challenge is to find a balance between trying to not overengineer simple solution and, at the same time, demonstrate architecutre skill and so on.
My solution could be a bit more on the 'too much' side, as for the application with one screen and a permission dialog, however, I wanted to demonstrate some more module approach, layer separation and other popular programming concepts and this is the reason why I selected to setup the app as it is set.

### Installation and run
as for the purpose of simplicity in this coding challenge I have hardcoded Google API key here (please read more about it below), project is ready to run immediately 
after being imported into Android Studio. No extra steps needed.

### Tech Stack Used and reasoning
#### MVVM 
I was thinking about going with Jetpack Compose, as it is the newest trend and it is alreaduy in stable release. However, for showing Google Maps it seems to be more complex than MVVM
#### ViewBinding
For the simplicity, I selected to not use databinding and to set the views in the code in activity. It increases the size of the class, however,
activity is responsible mainly for updating bidings and requesting permission. 
#### Hilt
DI 
#### Retrofit + Coroutines + Moshi + Flows

### What was left behind anf future improvements ##
#### Hiding secrets 
Google strongly suggests that the Api key should not be submitted into the GitHub. However, to spare the reviewers and myself some effort, I did committed my Api key anyways. 
The Key is restricted to the application by package name and signature and to be used Google Maps only, I will also delete it after the coding challenge review. 

The same goes for the 'secret-key' header, which should not be hardocded, and can be stored in some more secure way

#### Tests
Because of mostly time limitations 
- I did add less tests than I planned initially. I would definitely add some good UI tests to check the main user flows of the app
- I would unit-test in separation each class which allows it
There are an example tests of ViewModel and VehicleService though. 

#### LocationPermission
Separate object (probably child of LifecycleObserver) can be introduced to handle locationPermission request, it would make activity leaner. 

