# capstone_sam_alex

This week, we worked on getting the login screen working completely, populating the database with location every x minutes, and prompting notifications. 

The login screen last week consisted of a username and password field that would authenticate the user after checking the inputs against Firebase's Authentication Database. After our presentation last week, we had to change this since we didn't want the student using their email and also concluded that having a password is unnecessary. We will assign each user a "unique" username that they will use to login to the app. They will not require a password - we got rid of that field. Additionally, the Authentication Database will not contain the user's SLU email address - it will contain the unique participantID. Tracing back to the participant's name using this participantID will be hard so that gives us an additional layer of protection. We added code that checks to see if the user has previously signed into the app. This way, the participants won't have to sign in each time they open the app. We also created a sign out button and added code in our classes that allows the user to sign out. The login screen was time consuming but now it is finalized since we addressed all the concerns and features that came up last week.

The next thing we worked on is populating the databse with location updates (and time) every x minutes. This works now.  We created a service that will track the users location every x minutes and add it to the database.  This runs in the background of the app and will continue to run even if the app is closed.  This will run on our timer screen and will start and stop when the timer begins and ends. The only aspect of the location to be completed is getting the user to accept location updates should their location be turned off.  We had this functionality before deliverable 1, but because we changed to getting the location in a service, we need to change how this is done.

We also began working on the timer functionality.  We currently have a timer with a progress bar that will run for 30 minutes and then reset.  The next step is to display a notification when the timer ends and prompt the user to answer quesitons.