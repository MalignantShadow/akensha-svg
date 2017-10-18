# akensha-svg
Circuit Script (also called Akensha) is a writing system I made that is designed to look like circuits.Examples and a full description
about what it is can be read [here](https://malignantshdow.info/project/akensha).

# Usage
There are two ways to render a word. The first is by simply giving the program a `String`, and the second allows you to stylize the word
by giving it some numbers.

## With a String
This is the easiest way. The renderer uses the default sizing for the arms (1/3 or 2/3 of the letter height, depending on the arm).

```java
Renderer renderer = Renderer.getRenderer("Circuits", padding, letterHeight, strokeWidth);
// Note - the casing of the string does not matter, Akensha has no need for capitalization.
```

## Stylizing Letters
Letters can be stylized by supplying more arguments:

```java
List<LetterInfo> info = new ArrayList<LetterInfo>();
info.add(new LetterInfo(letter, lx, ly, rx, ly));
// ...
Renderer renderer = new Renderer(info, padding, letterHeight, strokeWidth);
```

### LetterInfo
* `letter` - The letter to use. A-Z constants are found in the `Letter` class. (e.g. `Letter.A`, `Letter.B` ...)
* `lx` - The length of the left arm before it bends (if it does)
* `ly` - The length of the left arm after it bends (if it does). This value is ignored for letters whose left arm does not bend,
  `lx` is used instead
* `rx`, `ry` - The same as `lx` and `ly`, but applied to the right arm

#### left() and right()
If you only want to stylize the left or right arm of a letter, you can use these methods. The default sizing will be used for the other arm.
This is also useful for letters that only have one arm.

```java
LetterInfo aInfo = LetterInfo.right(Letter.A, 50, 0);
LetterInfo dInfo = LetterInfo.left(Letter.T, 50, 0);
```


## Renderer Parameters
* `padding` - The amount of pixels to add around each edge of the image
* `letterHeight` - The height, in pixels, of a letter (100 recommended)
* `strokeWidth` - Used for the `stroke-width` property in each `<path>` element. (6 recommended)
