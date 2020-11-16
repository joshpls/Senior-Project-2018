# ParentTeacherMobileApplication
Parent-Teacher Conference Mobile Application developed in May 2018 for my Senior Project, titled Lets Meet.
Developed in: Android Studio SDK
Languages: Java, MySQL, PHP, jQuery, JavaScript, HTML, CSS
Lines of Code: ~3000

Description:
Teacher signs up / logs in. Writes or auto-generates her message to send. Selects contacts (parents), and sends the message. Upon parent receiving message, they will click link to continue to confirm their appointment on our website. Once confirmed, the teacher can see all confirmations inside the app.

Details:
Login/Register Authentication via HttpURLconnection with Php. Used SHA1 for security into MySQL database.
Bottom Drawer View for fragment view: Message, Confirmations, Settings.
Contact Picker to choose contacts, and stored into a ListView.
SendSMS method and stores needed information into database for confirmations.
Confirmation fragment pulls data from server and displays in a simple but elegant design.

Created by Joshua Kindelberger
