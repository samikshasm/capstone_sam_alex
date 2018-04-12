# capstone_sam_alex

This week, we finished all the core functionalities of the app. The timer works perfectly with 3 notifications going off every 30 minutes and a 4th notification going off at 8 am the next morning to notify the participant to fill out the morning questionnaire. The app automatically switches screens for the user depending on what notification just went off to limit the user access. We have a timer in the UI that displays the number of minutes remaining till the next notification goes off. The UI runs in the background so if the user exits the app and still has it running in the background, the UI will update. We are just fixing some issues in the onDestroy method (which is when the user exits completely out of the app). This is not a big deal in terms of the functionality of the app because users do not usually destroy apps completely. Additionally, the timers, alarms, and screen switching still happen at the right time - the UI just isn’t updating. We hope to fix that this week.
We also added questions to our question activity that the user needs to answer every time they add a drink. The user can choose and change their answers as many times as they want and just have to hit a submit button that will log all of their answers to the database. We are now working on the UI for this page. We organized the database and made sure that the data is being stored in a logical manner.
This week, we hope to meet with Dr. Shacham for a final “presentation” and ask any additional questions we have. We also plan on finishing all of the UI components as well as the morning questionnaire and really refine the appearance of the app. 