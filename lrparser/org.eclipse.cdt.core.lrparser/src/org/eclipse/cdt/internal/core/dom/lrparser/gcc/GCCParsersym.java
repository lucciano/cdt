/*******************************************************************************
* Copyright (c) 2006, 2008 IBM Corporation and others.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*     IBM Corporation - initial API and implementation
*********************************************************************************/

// This file was generated by LPG

package org.eclipse.cdt.internal.core.dom.lrparser.gcc;

public interface GCCParsersym {
    public final static int
      TK_auto = 26,
      TK_break = 33,
      TK_case = 34,
      TK_char = 45,
      TK_const = 21,
      TK_continue = 35,
      TK_default = 36,
      TK_do = 37,
      TK_double = 46,
      TK_else = 81,
      TK_enum = 57,
      TK_extern = 27,
      TK_float = 47,
      TK_for = 38,
      TK_goto = 39,
      TK_if = 40,
      TK_inline = 28,
      TK_int = 48,
      TK_long = 49,
      TK_register = 29,
      TK_restrict = 22,
      TK_return = 41,
      TK_short = 50,
      TK_signed = 51,
      TK_sizeof = 14,
      TK_static = 25,
      TK_struct = 58,
      TK_switch = 42,
      TK_typedef = 30,
      TK_union = 59,
      TK_unsigned = 52,
      TK_void = 53,
      TK_volatile = 23,
      TK_while = 32,
      TK__Bool = 54,
      TK__Complex = 55,
      TK__Imaginary = 56,
      TK_integer = 15,
      TK_floating = 16,
      TK_charconst = 17,
      TK_stringlit = 18,
      TK_identifier = 1,
      TK_Completion = 6,
      TK_EndOfCompletion = 3,
      TK_Invalid = 95,
      TK_LeftBracket = 43,
      TK_LeftParen = 2,
      TK_LeftBrace = 8,
      TK_Dot = 68,
      TK_Arrow = 82,
      TK_PlusPlus = 12,
      TK_MinusMinus = 13,
      TK_And = 11,
      TK_Star = 7,
      TK_Plus = 9,
      TK_Minus = 10,
      TK_Tilde = 19,
      TK_Bang = 20,
      TK_Slash = 69,
      TK_Percent = 70,
      TK_RightShift = 64,
      TK_LeftShift = 65,
      TK_LT = 71,
      TK_GT = 72,
      TK_LE = 73,
      TK_GE = 74,
      TK_EQ = 76,
      TK_NE = 77,
      TK_Caret = 78,
      TK_Or = 79,
      TK_AndAnd = 80,
      TK_OrOr = 83,
      TK_Question = 84,
      TK_Colon = 62,
      TK_DotDotDot = 66,
      TK_Assign = 63,
      TK_StarAssign = 85,
      TK_SlashAssign = 86,
      TK_PercentAssign = 87,
      TK_PlusAssign = 88,
      TK_MinusAssign = 89,
      TK_RightShiftAssign = 90,
      TK_LeftShiftAssign = 91,
      TK_AndAssign = 92,
      TK_CaretAssign = 93,
      TK_OrAssign = 94,
      TK_Comma = 60,
      TK_RightBracket = 67,
      TK_RightParen = 44,
      TK_RightBrace = 61,
      TK_SemiColon = 24,
      TK___attribute__ = 4,
      TK___declspec = 5,
      TK_ERROR_TOKEN = 31,
      TK_EOF_TOKEN = 75;

      public final static String orderedTerminalSymbols[] = {
                 "",
                 "identifier",
                 "LeftParen",
                 "EndOfCompletion",
                 "__attribute__",
                 "__declspec",
                 "Completion",
                 "Star",
                 "LeftBrace",
                 "Plus",
                 "Minus",
                 "And",
                 "PlusPlus",
                 "MinusMinus",
                 "sizeof",
                 "integer",
                 "floating",
                 "charconst",
                 "stringlit",
                 "Tilde",
                 "Bang",
                 "const",
                 "restrict",
                 "volatile",
                 "SemiColon",
                 "static",
                 "auto",
                 "extern",
                 "inline",
                 "register",
                 "typedef",
                 "ERROR_TOKEN",
                 "while",
                 "break",
                 "case",
                 "continue",
                 "default",
                 "do",
                 "for",
                 "goto",
                 "if",
                 "return",
                 "switch",
                 "LeftBracket",
                 "RightParen",
                 "char",
                 "double",
                 "float",
                 "int",
                 "long",
                 "short",
                 "signed",
                 "unsigned",
                 "void",
                 "_Bool",
                 "_Complex",
                 "_Imaginary",
                 "enum",
                 "struct",
                 "union",
                 "Comma",
                 "RightBrace",
                 "Colon",
                 "Assign",
                 "RightShift",
                 "LeftShift",
                 "DotDotDot",
                 "RightBracket",
                 "Dot",
                 "Slash",
                 "Percent",
                 "LT",
                 "GT",
                 "LE",
                 "GE",
                 "EOF_TOKEN",
                 "EQ",
                 "NE",
                 "Caret",
                 "Or",
                 "AndAnd",
                 "else",
                 "Arrow",
                 "OrOr",
                 "Question",
                 "StarAssign",
                 "SlashAssign",
                 "PercentAssign",
                 "PlusAssign",
                 "MinusAssign",
                 "RightShiftAssign",
                 "LeftShiftAssign",
                 "AndAssign",
                 "CaretAssign",
                 "OrAssign",
                 "Invalid"
             };

    public final static boolean isValidForParser = true;
}
