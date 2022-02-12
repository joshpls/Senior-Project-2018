# Let's Meet

A Parent-Teacher Conference Mobile Application developed in May 2018 for my Senior Project, titled Lets Meet.
Developed in: Android Studio SDK
Languages: Java, MySQL, PHP, jQuery, JavaScript, HTML, CSS
Lines of Code: ~3000

# Description:
- Created an Android application using Java and Android Studio to provide an easy solution to scheduling parent-teacher conferences.
- Mobile app generates a user specific link to a website for the parent via SMS to schedule the conference meeting.
- Teacher can sign up & log in the mobile application. Write or auto-generate their message to send. Selects all contacts (parents) from phone and sends the message with an auto-generated link specific to the parent. Upon parent receiving the message, they will click the link to continue to confirm their appointment on Let's Meets website. Once confirmed, the teacher can see and manage all the confirmations inside the app.

# Details:
- Login/Register Authentication via HttpURLconnection with Php. Used SHA1 for security into MySQL database.
- Bottom Drawer View for fragment view: Message, Confirmations, Settings.
- Contact Picker to choose contacts, and stored into a ListView.
- SendSMS method and stores needed information into database for confirmations.
- Confirmation fragment pulls data from server and displays in a simple but elegant design.

# Screenshots

![yAfN-T7g](https://user-images.githubusercontent.com/22349589/153697648-62bb90a4-dcbd-48bf-a567-4ece5112e0aa.png)

![yAfN-T7g_2](https://user-images.githubusercontent.com/22349589/153697656-c2037282-ce0f-420a-9a22-fa489ba8e807.png)

# Author:
- Created by Joshua Kindelberger
