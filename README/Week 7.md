# capstone_sam_alex

At the end of last week, we thought we had the countdown timer and alarms working. However, last weekend, we realized that the countdown timer logic was not correct because it did not keep running if the app was destroyed completely. We had to redo some of the code and added alarms instead of timers that run in the background. After we were able to get that to work, we had to figure out a different way of updating the UI which was difficult. The UI in the original code used to update as soon as the timer ended and Android has an onFinish() method for countdown timers. Alarms do not have that functionality. We had to figure out a way of having the UI constantly updating in the background so when the user comes back to the app, the UI matches the true number of minutes left in the alarm. Getting this to work and debugging the previous timer code took up most of our time this week. 

After getting that, we added a Start Activity and now we are trying to integrate the new activity with the Login and the Main classes. Once we get this working, we have to create a longer alarm for the morning questionnaire which will prompt the user the next morning to fill it out. 

We are making good progress even though we were set back at the beginning of this week. Once we have all of the alarm/timer code working, we do not forsee adding question screens and updating answers to the database as a particularly difficult task. Next week, we will continue working on the timer/alarm code, which keeps getting more complicated, and hopefully will have that working so that we can get to the last bit of the app. 