# time tracker system (TTS)

This is small project (crud) to keep working hours out of company network, to make reports faster and easier.
Data doesn't need to be fully secured since it is not directly connecetd with company tools.

It is more like hours notes, report is very simple:
* working hours - from, to
* break time - from, to
* type - office or WFH (work from home)

## Tech stack
* kotlin
* graphQL/rest (for learning aspects rather than wise decision of usage)
* JWT

## API
    
Receiving JWT token    
```sh
curl -i -X POST -H "Content-Type: application/json" -d '{"username":"myName","password":"secret123"}' http://localhost:5000/app/login
```

response will have token:
```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJhcHAiOiJUaW1lUmVwb3J0Iiwic2FsdCI6ImRlZmF1bHRTYWx0IiwiZXhwIjoxNTQ4OTMxNjAwLCJ1c2VybmFtZSI6Im1pY2hhbCJ9.xunWPd14fk2DyB7n2qUmNNWey-0X0GGoYkyAr5vrvNs
```

Invalidating token (logging out from all places)
```jshelllanguage
curl -i -X POST -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJhcHAiOiJUaW1lUmVwb3J0Iiwic2FsdCI6ImRlZmF1bHRTYWx0IiwiZXhwIjoxNTQ4OTMxNjAwLCJ1c2VybmFtZSI6Im1pY2hhbCJ9.xunWPd14fk2DyB7n2qUmNNWey-0X0GGoYkyAr5vrvNs"  https://tts-reporter.herokuapp.com/app/v1/logout
```

### REST API

Get the time report data
```jshelllanguage
curl -i -X GET -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJhcHAiOiJUaW1lUmVwb3J0Iiwic2FsdCI6ImRlZmF1bHRTYWx0IiwiZXhwIjoxNTQ4OTMxNjAwLCJ1c2VybmFtZSI6Im1pY2hhbCJ9.xunWPd14fk2DyB7n2qUmNNWey-0X0GGoYkyAr5vrvNs"  http://localhost:5000/app/v1/timereport/2018/12
```

Get the time report data (with all day events (those closed as well))
```jshelllanguage
curl -i -X GET -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJhcHAiOiJUaW1lUmVwb3J0Iiwic2FsdCI6ImRlZmF1bHRTYWx0IiwiZXhwIjoxNTQ4OTMxNjAwLCJ1c2VybmFtZSI6Im1pY2hhbCJ9.xunWPd14fk2DyB7n2qUmNNWey-0X0GGoYkyAr5vrvNs"  http://localhost:5000/app/v1/timereport/2018/12?onlyActiveDays=false
```

Updating day event
```jshelllanguage
curl -i -X PUT -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJhcHAiOiJUaW1lUmVwb3J0Iiwic2FsdCI6IjM1M2U3YmQ0LWZiMDUtNDAzNy04MWIwLWVmNDY2NjA2Y2JlNiIsImV4cCI6MTU0ODk2Njg0NCwidXNlcm5hbWUiOiJtaWNoYWwifQ._oxRd4YSyZV_s9MymXVTHqYLPu-75gd57rMrPc33iOk"   -H "Content-Type: application/json" -d '{"date":"2018-12-11","type":"WFH","workTime":{"from":"09:00:00","to":"17:00:00"},"breakTime":{"from":"12:00:00","to":"12:30:00"}}' http://localhost:5000/app/v1/timereport
```

## Hints

for initial encoding password: ```test``` = ```MD5}098f6bcd4621d373cade4e832627b4f6```


## Deploying to Heroku

```sh
$ heroku create
$ git push heroku master
$ heroku open
```

## Documentation

For more information about using Java and Kotlin on Heroku, see these Dev Center articles:

- [Java on Heroku](https://devcenter.heroku.com/categories/java)
