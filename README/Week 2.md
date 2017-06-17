# capstone_sam_alex

This week we worked on setting up a connection to the Firebase authentication database. We had to first setup Firebase in Android Studio and import certain files into the app in order for this connection to become successful.

Once we got the network connection, we created a Login Activity with certain features. When we talked to Dr. Shacham, we decided that we do not want participants to be able to create a new account within the app - they will be assigned unique user IDs and passwords that they must use. Hence, we added methods that only check if the entered username and password matched a pair in the database. There are other methods in the activity that handle other features. 

We believe that we have all the necessary methods in place and the network connection is set up correctly. However, for the past couple days, we have both been receiving different errors that we are having trouble fixing. Last week, before we found SourceTree, we tried integrating GitHub into Android Studio using Version Control, but that messed up the functionality of the software and we think that this is causing the errors to pop up while running the gradle. The goal is to fix this issue by this weekend and hopefully have a functioning login screen.

The goal for the next week is to feed data from the app (can be any data) to a different table in Firebase. Once we have this working, we will have the foundation of the app on which we can start adding features in the coming weeks. 
