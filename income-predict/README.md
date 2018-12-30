#Predicting individual's income range
-------------------------------------

#About the project
1) This project is applying machine learning algorithm (Kmeans clustering) and Hadoop MapReduce 	software framework to data sets which consist of  individual income details.
2) The motive of the project is to predict the income range of an individual and to provide a market 	value of a person with respect to various factors such as education, skill set and so on. 
3) This is unique service which can be given to the people for knowing their actual market worth and 	expected salary range. From these prediction models, the average income for person's work in the 	industry can be estimated. 
4) Then consider how he can combine his skills and experience in relation to the average person to 	determine the market value for his specific combination of skills and experience in the 	industry. 
5) In short, we will be able to find a broad average salary for specific job in the industry.


----------------------------------------------------------------------------------------------------
# Prerequisites
---------------
##place the  kmeans-hd.jar file in following location:

1)Export the Kmeans-hd project as jar or use the jar given in the name of "kmeans-hd.jar"
2)Place the jar in this Location->C:\tmp\enterprise\hadoop
3)jar file name->kmeans-hd.jar


##place the income.txt and centroid.txt to hadoop filesystem

1)open cmd(as admin) and run hadoop

2)
a. Go to location of income data and Run the following command, which will put the income.txt in HDFS in /ec folder.
###	hadoop fs -put income.txt /ec

b. PLease note format of centroid file in lab and this project is different.First create a centroid.txt. The below is the format
24,43,0,25,60,0,40,21000	0
56,45,8,50,60,15,40,48000	1
86,29,20,100,100,23,40,105000	2

first 8 number in each row are the actual values and the 9th last value in each row is row number like row0,row1...
###  In centriod, enter 8 values separated by "," and give "tab" ,then enter row no (dont give space, it won't read) .Make sure your centroid has this format.

then use this command to put the centroid.txt in HDFS in /ec folder.
###	hadoop fs -put centroid.txt /ec
----------------------------------------------------------------------------------------------------

#Preloaded Users in db
-----------------------
a)User details
UserRole	Username		Password
1			tom				jerry
2			rock			bottom
3			romeo			juliet
3			ananth			hari

b) User Role "1" is for Developer page. 
c)  User Role "2" is for admin page. 
d) User Role "3" is for General user page. 


#Doxygen and Log4j
-----------------------
1) Doxygen tool is used to generate documentation

2)Using Log4j, Logs are recorded in the following 
####location-->C:\\logs\\loginfo.log

#RESTful webservice
-----------------------
1) The income-rs project provides the webservice needed for this project.
2) So deploy income-rs project.
3) webservice are implemented in the help button(found in navigation bar) which are found in admin,general and developer webpages.


#Junit testing
-----------------------
1)Junit is implemented to test the services.

2)model training features are tested in Junit. So Make sure the result2 folder is deleted in hadoop file system before using this testing service.

3)This is generate the clusters and model will be trained and generate in HDFS under the following location -->/result2



----------------------------------------------------------------------------------------------------


##deploy projects

1) deploy income-predict and income-rs projects into wildfly server

Note--> income-rs is the Restful webservice project and this is used by the income-predict project.

Note--> In each page there will be help button which guides what input the user should give.

Note--> Install "XAMP" and start Apache and MySQL before starting the server. For viewing the Db 		enter following URL--http://localhost/phpmyadmin/index.php?lang=en

Note--> Also start the hadoop system before running the project.





-----------------------------------------------------------------------------------------------------
##instruction on how to run the project

-->When you run the project, the starting page is login page. It will redirect to any of 3 different pages according to the username who is logging in(based on user role)

-->Admin page---Features to change the user role for existing user.
-->Developer page---It has 2 features, a)To train model   b) To save the trained model to Db.
-->General page---Features has the option to feed input factors and income range will be predicted.

### Admin Page
1)Enter the existing user name and their new role. The new role will be updated

----------------------------------------------------------------------------------------------------

### Developer Page

####Train Model button
----------------------
1) When we click this button, we build the model using hadoop MapReduce.

2) The output is stored in the path - / result2 of HDFS

3) Now we need to note the exact output path for next operation

4)Use the below command
####  hadoop fs -ls /result2
this will result in 
Found 2 items
drwxr-xr-x   - anant supergroup          0 2018-11-19 11:49 /result2/151445576039561
drwxr-xr-x   - anant supergroup          0 2018-11-19 11:49 /result2/151461288131317

5) Use the below command
#### hadoop fs -ls /result2/151461288131317
this will result in
Found 2 items
-rw-r--r--   1 anant supergroup          0 2018-11-19 11:49 /result2/151461288131317/_SUCCESS
-rw-r--r--   1 anant supergroup        216 2018-11-19 11:49 /result2/151461288131317/part-00000

6) Now use this command to see the result
####  hadoop fs -cat /result2/151461288131317/part-00000

7) the trained model location is /result2/151461288131317/part-00000



#### Save Model button
---------------
1)in the text box, enter the trained model location which is found in previous steps.
for example say this below is path-->/result2/151461288131317/part-00000

2)Enter the name of model in which it has to be stored in Db.

3)once we click the save button, the model is saved in the Db .Go and check if it is created.


----------------------------------------------------------------------------------------------------

###General page

#### Predict button

1) Enter the 8 input and get your predicted income range.

Note-->In Model Name input text, give the name of the model which is stored in Db. 



