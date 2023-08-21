# Commands Voice Recognition PoC

## Voice Recognition

### We want to achieve:
- offline support
- different languages support
- infinite recording 
- free usage

### Options:
- https://cloud.google.com/speech (and other 3rd party solutions)
  - online only
  - supports a lot of languages
  - supports infinite recording
  - expensive
- https://developer.android.com/reference/android/speech/SpeechRecognizer
  - by default, offline+online, but can be offline only
  - supports a lot of languages
  - doesn't support infinite loading
  - free
- Custom ML model
  - offline only
  - languages are limited by developers resources
  - supports infinite recording
  - free for usage, but takes more resources to setup own model

### ML Model
There are plenty ML models on the market, but most of them are not usable on Android (TensorFlow Lite).

Due to limited resources, decision was made to train ML only for small set of commands, which can be found in network.

I took https://www.tensorflow.org/datasets/catalog/speech_commands and picked only words - 'go', 'follow', 'off', 'stop' and digits ('zero' to 'nine').
This dataset has ~2000 samples for each word

Model training is described in this article - https://www.tensorflow.org/lite/models/modify/model_maker/speech_recognition

### Model Result
Trained model didn't show great results. It produces a lot of mistakes (f.e. "off" can be transcribed as "four", doesn't always recognize words as well).

### Users generated ML Model
In the future, having data from users, we can improve shared ML Model or train Model for every particular user to get better results

### Voice Recognition Decision

I believe, that own ML Model has a lot of benefits, but it requires much more resources to create model, which will show accepted results.
If time/human resources are limited, 3rd party solutions can be great solution

## Commands

### Initial Implementation
This initial implementation is based on implementation of commands in Kotlin.

Abstraction `CommandStackInteractor` is created to encapsulate commands stack logic and provide APIs for commands

There are 2 types of commands:
- `CommandWithInput`
- `InstantCommand`

To create a new command:
1. Create new class and inherit required interface
2. Create new class and inherit `CommandProvider` to describe how to provide commands factory method and predicate when command should be create
3. Add `CommandProvider` to the `CommandFactory` during binding it using Hilt by adding `@IntoSet`

### Possible Improvements

Having `CommandStackInteractor`, we can describe commands using JSON and get commands from the backend. 
By passing current code version, backend will be able to provide commands suited only to current `CommandStackInteractor` implementation.

Possible JSON is:
```
{
    "instant" : [
        { 
            "title": "back",
            "operations": [
                "removeLast"
            ],
            "predicate": {
                "key" : "back"
            }
        }
    ],
    "withInputs": [
        {
            "title": "code",
            "predicate" : {
                "key" : "code"
            }
        }
    ]
}
```