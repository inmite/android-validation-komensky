# ValidationKomensky for Android
Library for validating user input easily.

Just annotate view fields with validation annotations and you are ready to validate user input with one line of code.

![alt text](https://raw.github.com/inmite/android-validation-komensky/master/graphics/demo.png "user input validations")

Features:

 - Validate all views at once and show feedback to user. With one line of code.
 - Live validation - check user input as he moves between views with immediate feedback.
 - Extensible library - you can add your own validations or adapters for custom views.

## How to include it in your project:

~~With Maven:~~ (not yet, it will get in central soon)

    <dependency>
		<groupId>eu.inmite.android.lib</groupId>
		<artifactId>android-validation-komensky</artifactId>
		<version>0.9.2</version>
		<type>jar</type>
    </dependency>

Or:

 - clone the project
 - add it as library project in your IDE

## How to validate

First, annotate your views:


	@NotEmpty(messageId = R.string.validation_name, order = 1)
	private EditText mEditName;

	@DateInFuture(messageId = R.string.validation_date)
    private TextView mTxtDate;

Now you are ready to:

    FormValidator.validate(this, new SimpleErrorPopupCallback(this));

You will receive collection of all failed validations in a callback and you can present them to the user as you want or simply use prepared callbacks (like SimpleErrorPopupCallback).

To start and stop live validation, simply call:

	FormValidator.startLiveValidation(this, new SimpleErrorPopupCallback(this));
	FormValidator.stopLiveValidation(this);

Validations supported out of the box:
 - NotEmpty
 - Length
 - MaxLength
 - MinLength
 - MaxValue
 - MaxNumberValue
 - MinValue
 - MinNumberValue
 - RegExp
 - DateInFuture
 - DateNoWeekend
 - Custom

And you can always create your own validation. 
