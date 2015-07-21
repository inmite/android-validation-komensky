# ValidationKomensky for Android
A simple library for validating user input in forms using annotations.

![alt text](https://raw.github.com/inmite/android-validation-komensky/master/graphics/demo.png "user input validations")

Features:

 - Validate **all views at once** and show feedback to user. _With one line of code._
 - **Live validation** - check user input as he moves between views with immediate feedback.
 - **Extensible** library - you can add your own validations or adapters for custom views.

## How to include it in your project:

With Gradle:
```groovy
compile 'eu.inmite.android.lib:android-validation-komensky:0.9.4@aar'
```

Manually:

 - clone the project
 - add it as library project in your IDE

## How to validate

First, annotate your views like this:
```java
@NotEmpty(messageId = R.string.validation_name)
@MinLength(value = 3, messageId = R.string.validation_name_length, order = 2)
private EditText mNameEditText;
```

Now you are ready to:
```java
FormValidator.validate(this, new SimpleErrorPopupCallback(this));
```

You will receive collection of all failed validations in a callback and you can present them to the user as you want.
Or simply use prepared callbacks (like `SimpleErrorPopupCallback`).

### Live validation

To start and stop live validation, simply call:
```java
FormValidator.startLiveValidation(this, new SimpleErrorPopupCallback(this));
FormValidator.stopLiveValidation(this);
```

### List of all supported validation annotations

Validations supported out of the box:
 - [NotEmpty](../master/library/src/main/java/eu/inmite/android/lib/validations/form/annotations/NotEmpty.java)

```java
@NotEmpty(messageId = R.string.validation_name, order = 1)
private EditText mNameEditText;
```
 - [MaxLength](../master/library/src/main/java/eu/inmite/android/lib/validations/form/annotations/MaxLength.java)
 - [MinLength](../master/library/src/main/java/eu/inmite/android/lib/validations/form/annotations/MinLength.java)

```java
@MinLength(value = 1, messageId = R.string.validation_participants, order = 2)
private EditText mNameEditText;
```
 - [MaxValue](../master/library/src/main/java/eu/inmite/android/lib/validations/form/annotations/MaxValue.java)
 - [MinValue](../master/library/src/main/java/eu/inmite/android/lib/validations/form/annotations/MinValue.java)

```java
@MinValue(value = 2L, messageId = R.string.validation_name_length)
private EditText mEditNumberOfParticipants;
```
 - [MaxNumberValue](../master/library/src/main/java/eu/inmite/android/lib/validations/form/annotations/MaxNumber.java)
 - [MinNumberValue](../master/library/src/main/java/eu/inmite/android/lib/validations/form/annotations/MinNumberValue.java)

```java
@MinNumberValue(value = "5.5", messageId = R.string.validation_name_length)
private EditText mEditPotentialOfHydrogen;
```
 - [RegExp](../master/library/src/main/java/eu/inmite/android/lib/validations/form/annotations/RegExp.java)

```java
@RegExp(value = EMAIL, messageId = R.string.validation_valid_email)
private EditText mEditEmail;
@RegExp(value = "^[0-9]+$", messageId = R.string.validation_valid_count)
private EditText mEditCount;
```
 - [DateInFuture](../master/library/src/main/java/eu/inmite/android/lib/validations/form/annotations/DateInFuture.java)

```java
@DateInFuture(messageId = R.string.validation_date)
private TextView mTxtDate;
```
 - [DateNoWeekend](../master/library/src/main/java/eu/inmite/android/lib/validations/form/annotations/DateNoWeekend.java)

```java
@DateNoWeekend(messageId = R.string.validation_date_weekend)
private TextView mTxtDate;
```
 - [Custom](../master/library/src/main/java/eu/inmite/android/lib/validations/form/annotations/Custom.java)

```java
@Custom(value = MyVeryOwnValidator.class, messageId = R.string.validation_custom)
private EditText mNameEditText;
```

## Proguard

Should you need to use the proguard, copy these rules:

```proguard
-keepattributes *Annotation*
-keep class eu.inmite.android.lib.validations.form.annotations.** { *; }
-keep class * implements eu.inmite.android.lib.validations.form.iface.ICondition
-keep class * implements eu.inmite.android.lib.validations.form.iface.IValidator
-keep class * implements eu.inmite.android.lib.validations.form.iface.IFieldAdapter
-keepclassmembers class ** {
	@eu.inmite.android.lib.validations.form.annotations.** *;
}
```

## Why 'Komensky'?

<img src="http://upload.wikimedia.org/wikipedia/commons/c/ce/Johan_amos_comenius_1592-1671.jpg" width="70"  align="right"/>

[Jan Ámos Komenský](http://en.wikipedia.org/wiki/John_Amos_Comenius)  was a famous Czech educator, father of modern education methods.&nbsp;
Teachers tend to correct you, just like this library. You won't miss any errors in the user input.

See [**our other Czech personalities**](http://inmite.github.io) who help with [#AndroidDev](https://plus.google.com/s/%23AndroidDev).
