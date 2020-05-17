package andrasferenczi.configuration

import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import javax.swing.*

// Insets
private const val TOP_INSET = 10
private const val BOTTOM_INSET = 0
private const val RIGHT_INSET = 10

// The texts should be aligned
private const val CHECKBOX_CHECK_AREA_WIDTH = 22
private const val CHECKBOX_LEFT_INSET = 15
private const val NO_CHECKBOX_LEFT_INSET = CHECKBOX_CHECK_AREA_WIDTH + CHECKBOX_LEFT_INSET


fun createConfigurationUI(input: ConfigurationData): ConfigurationUIElements {

    val pane = JPanel(GridBagLayout())

    // ROW 1
    pane.add(
        JLabel("name of the copy method"),
        GridBagConstraints().apply {
            fill = GridBagConstraints.HORIZONTAL
            gridx = 0
            gridy = 0
            insets = Insets(TOP_INSET, NO_CHECKBOX_LEFT_INSET, BOTTOM_INSET, RIGHT_INSET)
        }
    )

    val copyWithNameTextField = JTextField(input.copyWithMethodName)
    pane.add(
        copyWithNameTextField,
        GridBagConstraints().apply {
            fill = GridBagConstraints.HORIZONTAL
            weightx = 0.6
            gridx = 1
            gridy = 0
            insets = Insets(TOP_INSET, CHECKBOX_LEFT_INSET, BOTTOM_INSET, RIGHT_INSET)
        }
    )

    // ROW2
    pane.add(
        JLabel("name of the toMap method"),
        GridBagConstraints().apply {
            fill = GridBagConstraints.HORIZONTAL
            gridx = 0
            gridy = 1
            insets = Insets(TOP_INSET, NO_CHECKBOX_LEFT_INSET, BOTTOM_INSET, RIGHT_INSET)
        }
    )
    val toMapMethodNameTextField = JTextField(input.toMapMethodName)
    pane.add(
        toMapMethodNameTextField,
        GridBagConstraints().apply {
            fill = GridBagConstraints.HORIZONTAL
            weightx = 0.6
            gridx = 1
            gridy = 1
            insets = Insets(TOP_INSET, CHECKBOX_LEFT_INSET, BOTTOM_INSET, RIGHT_INSET)
        }
    )

    // ROW3
    pane.add(
        JLabel("name of the fromMap method"),
        GridBagConstraints().apply {
            fill = GridBagConstraints.HORIZONTAL
            gridx = 0
            gridy = 2
            insets = Insets(TOP_INSET, NO_CHECKBOX_LEFT_INSET, BOTTOM_INSET, RIGHT_INSET)
        }
    )

    val fromMapMethodNameTextField = JTextField(input.fromMapMethodName)
    pane.add(
        fromMapMethodNameTextField,
        GridBagConstraints().apply {
            fill = GridBagConstraints.HORIZONTAL
            weightx = 0.6
            gridx = 1
            gridy = 2
            insets = Insets(TOP_INSET, CHECKBOX_LEFT_INSET, BOTTOM_INSET, RIGHT_INSET)
        }
    )

    // ROW 4
    val useRequiredAnnotationCheckBox = JCheckBox(
        "add @required to constructor parameters",
        input.useRequiredAnnotation
    )

    pane.add(
        useRequiredAnnotationCheckBox,
        GridBagConstraints().apply {
            fill = GridBagConstraints.HORIZONTAL
            gridx = 0
            gridy = 3
            insets = Insets(TOP_INSET, CHECKBOX_LEFT_INSET, BOTTOM_INSET, RIGHT_INSET)
        }
    )

    // ROW 5
    val useNewKeywordCheckbox = JCheckBox(
        "use the 'new' keyword",
        input.useNewKeyword
    )
    pane.add(
        useNewKeywordCheckbox,
        GridBagConstraints().apply {
            fill = GridBagConstraints.HORIZONTAL
            gridx = 0
            gridy = 4
            insets = Insets(TOP_INSET, CHECKBOX_LEFT_INSET, BOTTOM_INSET, RIGHT_INSET)
        }
    )

    // ROW 6
    val useConstKeywordForConstructorCheckbox = JCheckBox(
        "use the 'const' keyword for the constructor if possible",
        input.useConstForConstructor
    )
    pane.add(
        useConstKeywordForConstructorCheckbox,
        GridBagConstraints().apply {
            fill = GridBagConstraints.HORIZONTAL
            gridx = 0
            gridy = 5
            insets = Insets(TOP_INSET, CHECKBOX_LEFT_INSET, BOTTOM_INSET, RIGHT_INSET)
        }
    )

    // ROW 7
    val optimizeConstCopyCheckbox = JCheckBox(
        "copy function should return the same instance if the only passed in variables are private"
    )
    pane.add(
        optimizeConstCopyCheckbox,
        GridBagConstraints().apply {
            fill = GridBagConstraints.HORIZONTAL
            gridx = 0
            gridy = 6
            insets = Insets(TOP_INSET, CHECKBOX_LEFT_INSET, BOTTOM_INSET, RIGHT_INSET)
        }
    )

    // ROW 8
    val addKeyMapperForMapCheckbox = JCheckBox(
        "add key mapper function parameter for `toMap` and `fromMap`"
    )

    pane.add(
        addKeyMapperForMapCheckbox,
        GridBagConstraints().apply {
            fill = GridBagConstraints.HORIZONTAL
            gridx = 0
            gridy = 7
            insets = Insets(TOP_INSET, CHECKBOX_LEFT_INSET, BOTTOM_INSET, RIGHT_INSET)
        }
    )

    // ROW 9
    val noImplicitCastsCheckbox = JCheckBox(
        "no implicit casts"
    )

    pane.add(
        noImplicitCastsCheckbox,
        GridBagConstraints().apply {
            fill = GridBagConstraints.HORIZONTAL
            gridx = 0
            gridy = 8
            insets = Insets(TOP_INSET, CHECKBOX_LEFT_INSET, BOTTOM_INSET, RIGHT_INSET)
        }
    )

    // Remaining space
    pane.add(
        JPanel(),
        GridBagConstraints().apply {
            weighty = 1.0   // request any extra vertical space
            anchor = GridBagConstraints.PAGE_END // bottom of space
            gridy = 9
            gridwidth = 2
        }
    )

    return ConfigurationUIElements(
        pane,
        copyWithNameTextField,
        toMapMethodNameTextField,
        fromMapMethodNameTextField,
        useRequiredAnnotationCheckBox,
        useNewKeywordCheckbox,
        useConstKeywordForConstructorCheckbox,
        optimizeConstCopyCheckbox,
        addKeyMapperForMapCheckbox,
        noImplicitCastsCheckbox
    )
}