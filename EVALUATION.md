# EVALUATION

| Evaluator     | Evaluatee      |
| ------------- | -------------- |
| Terhi Salonen | Hanna Sepänmaa |

# Application on device

| Device used for testing  | Android version |
| ------------------------ | --------------- |
| Motorola moto g(8) power | 11              |

## Usability and technical features

**Technical features**
|Current location|Given location|
|----------------|--------------|
|Current weather|Current weather|
|5 day forecast |5 day forecast|

**Pros**

- App is intuitive, meaning it is simple and clear enough for the user to use correctly, without any aid of tutorials or such.
- Graphic User Interface is nice looking and it runs smoothly.
- App returns fast results.
- Information given within the search result is presented in a clean and readable way. The icons and paragraphing makes the result(s) intresting and easy for eye.

**Cons**

- Screen orientation is locked to portrait.

**Other notes**

- User cannot search for location, which name contains hyphenation. This is not due to application itself, but the underlying api (openweathermap) seems to lack the capability to find hyphened locations. This was tested on both the app and the underlying api with names Yli-Tornio, FI and Lauda-Köningshofen, DE.
- Although application's functionalities are few, they work as is expected, and for the sake of clarity it is rather nice that the app does not also try to fill your tax return or some such thing, but focuses on the specific subject alone.

# Code

## README.md

The README.md is clear in a way that it is easy to read and follow. It includes all that was required.

## Comments in code

The code was commented in the most perfect way. There were comments to ease the following of the code, but not too much to make it cumbersome.

## Clarity of code

The code itself was written in such a descriptive manner that it made the following of the code easy enough even if there had not been any comments.

## Reflecting to project requirements

In my opinion this application meets the requirements of grade four. The only things I see that hold it a bit down (and thus, not reaching five) are the orientation locking to vertical and darkmode not applicable. The usability otherwise is great, documentation whether regarding README.md or the code itself, is impeccable and the app uses both RESTful API and device API.
