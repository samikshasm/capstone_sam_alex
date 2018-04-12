# capstone_sam_alex

This week, we continued creating and modifying the timer. The time for how long the timer goes is hard coded in as 30 mintues and ideally, once the user clicks the button, the number of drinks is incremented and the timer resets. We are working on that this week. 

Another main part of the app was to display a notification after 30 minutes was up. We were able to accomplish this and the notification service runs in the background (similar to the location service) so that the notifications and timer still runs if the app is not in use or the phone is locked. The notification also has buttons (or actions) that switch to a question screen if clicked. This screen does not have questions yet, if you look at the code, but that won't be too difficult to add. The harder part was to get the notification to switch to the question screen. 

We also met with Dr. Shacham on Monday to discuss the UI, the questions in the app, and the ap in general. She was extremely pleased with how the app is looking and gave us some good feedback. She has no problem with using Firebase and we agreed that we will give her a list of 500 unique participant ids that she would be able to use. Another thing she mentioned is that the IRB might require a password field along with the userID. Since we have a unique and long enough string of letters/numbers (definitely secure enough), we might be able to avoid this requirement but she is checking with them and if that's the case, we will have to bring back the password field. 

This weekend, we are working on getting the notification to pop up every 30 minutes for a total of 90 minutes. But if there is a user input/response, the counter will reset and the app will wait for another 90 minutes of no activity from the user before shutting down. This is the last "difficult" part of the app, in our opinion. So hopefully, if we get this done this weekend, we can proceed with adding the different screens and quesions. After that is acheived, we will start working on the question screens and start writing answers to the database. This will require organizing the tree and knowing where to store information. We will also be labeling the separate buttons so that it is easy for us to identify the user's resposnses. 

We really want to finish the major parts of the app this week so that if we have time at the end, we can add additional features, perfect the app, or run test cases so that we can identify bugs. 