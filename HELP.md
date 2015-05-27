<img src="https://github.com/deathgrindfreak/clashcallermobile/blob/master/img/cclogo.png">

# Clash Caller Mobile Help

Clash Caller Mobile is an Android app designed to interact with the [Clash Caller](http://clashcaller.com) site through its API.  For those unfamilar with the Clash Caller website, it intends to help in organizing wars for the popular [Clash of Clans](http://supercell.com/en/games/clashofclans/) mobile game by allowing players to "call" bases to attack as well as chronicling the results of their attacks for others to see.  

## Main View

The main view should be the first thing you see when the app loads.  The first view we'll cover is the Start War View which allows clan leaders and co-leaders (or other designated individuals) to start a war from their phone.

<img src="https://github.com/deathgrindfreak/clashcallermobile/blob/master/img/mainactivity.png">

## Start War View

The only required fields are the first three shown under the "Start War" header.  The first two simply designate the name for your clan and the name of the enemy clan as well as the size of the war.

The other options shown under the "More Options" header are purely optional, but can enhance the war experience if you choose to employ them.  The timer allows calls to be dropped after a certain time (this can prevent members from camping on enemy villages for too long).  The other two fields allow you to set the village ids for both your clan and the enemy clan and the final checkbox allows you to archive your war (this prevents the server from deleting your war after a period of time).

<img src="https://github.com/deathgrindfreak/clashcallermobile/blob/master/img/startwaractivity.png">

## Join War View

After a war has been created, admins will typically share the url with other members.  Simply enter the six character generated id and click submit to show the war (for example, if the url is http://clashcaller.com/abcdef then abcdef will be the war id).

<img src="https://github.com/deathgrindfreak/clashcallermobile/blob/master/img/joinwaractivity.png">

## War View

The War View is the most complex view of the app and can appear differently depending on the orientation of your device (if your device isn't wide enough, some content won't appear.  Turning your device to the landscape orientation will display the content properly).

<img src="https://github.com/deathgrindfreak/clashcallermobile/blob/master/img/waractivity1.png">

To make a call simply click the plus button next to the number of the village you wish to attack.

<img src="https://github.com/deathgrindfreak/clashcallermobile/blob/master/img/waractivitycall1.png">
<img src="https://github.com/deathgrindfreak/clashcallermobile/blob/master/img/waractivitycall2.png">

You can also set a comment for a particular enemy village (this can help others who may wish to attack the same base later on).

<img src="https://github.com/deathgrindfreak/clashcallermobile/blob/master/img/waractivitycomment1.png">
<img src="https://github.com/deathgrindfreak/clashcallermobile/blob/master/img/waractivitycomment2.png">

Admins can also set the clan message for the war as well (technically any user can, but since every member will be able to view it, most clans leave this duty to leaders and co-leaders).

<img src="https://github.com/deathgrindfreak/clashcallermobile/blob/master/img/waractivityclanmessage1.png">
<img src="https://github.com/deathgrindfreak/clashcallermobile/blob/master/img/waractivitysetmessage.png">

Turning your phone sideways will show the star view (tablets should have this view regardless).

<img src="https://github.com/deathgrindfreak/clashcallermobile/blob/master/img/waractivitylandscape.png">

Any number of members can call a single enemy base.  To add another call for a base, simply click the plus button again (the call will appear inline, below the last call for the target).

<img src="https://github.com/deathgrindfreak/clashcallermobile/blob/master/img/waractivitysecondcall.png">
<img src="https://github.com/deathgrindfreak/clashcallermobile/blob/master/img/waractivitysecondcall2.png">

In order to delete a call, simply click the "X" button.

<img src="https://github.com/deathgrindfreak/clashcallermobile/blob/master/img/waractivitydeletecall.png">
<img src="https://github.com/deathgrindfreak/clashcallermobile/blob/master/img/waractivitydeletecall2.png">

To set the stars gained during an attack, click the button next to the member button.

<img src="https://github.com/deathgrindfreak/clashcallermobile/blob/master/img/waractivitysetstar.png">
<img src="https://github.com/deathgrindfreak/clashcallermobile/blob/master/img/waractivitysetstar2.png">

## History View

After a war has been either successfully created or joined, its information will be saved to the history view for quick viewing.  Simply click the button with appropriate information in order to visit the war.

<img src="https://github.com/deathgrindfreak/clashcallermobile/blob/master/img/historyactivity.png">

To delete a war from your history, press the "X" button.

<img src="https://github.com/deathgrindfreak/clashcallermobile/blob/master/img/historyactivitydelete.png">

If you've made a mistake in spelling, you can alter the name of the call by clicking on the name.

<img src="https://github.com/deathgrindfreak/clashcallermobile/blob/master/img/waractivitysetclan.png">

## Settings

Since both your clan, clan id and village name are unlikely to change very often, you can save this information in the settings view (this is accessible from the navigation drop down).  That way everytime you start a war or add your name to a call, the saved information will automatically be filled in.

<img src="https://github.com/deathgrindfreak/clashcallermobile/blob/master/img/settingsactivity.png">