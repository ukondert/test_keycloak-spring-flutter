import 'package:flutter/material.dart';
import '../../design_tokens.dart';

/// Atom: Text component with consistent styling
class AppText extends StatelessWidget {
  final String text;
  final TextStyle? style;
  final Color? color;
  final TextAlign? textAlign;
  final int? maxLines;
  final TextOverflow? overflow;

  const AppText(
    this.text, {
    super.key,
    this.style,
    this.color,
    this.textAlign,
    this.maxLines,
    this.overflow,
  });

  factory AppText.headline(
    String text, {
    Key? key,
    Color? color,
    TextAlign? textAlign,
  }) {
    return AppText(
      text,
      key: key,
      style: AppTypography.headlineSmall,
      color: color,
      textAlign: textAlign,
    );
  }

  factory AppText.title(
    String text, {
    Key? key,
    Color? color,
    TextAlign? textAlign,
  }) {
    return AppText(
      text,
      key: key,
      style: AppTypography.titleLarge,
      color: color,
      textAlign: textAlign,
    );
  }

  factory AppText.body(
    String text, {
    Key? key,
    Color? color,
    TextAlign? textAlign,
    int? maxLines,
    TextOverflow? overflow,
  }) {
    return AppText(
      text,
      key: key,
      style: AppTypography.bodyMedium,
      color: color,
      textAlign: textAlign,
      maxLines: maxLines,
      overflow: overflow,
    );
  }

  factory AppText.label(
    String text, {
    Key? key,
    Color? color,
    TextAlign? textAlign,
  }) {
    return AppText(
      text,
      key: key,
      style: AppTypography.labelMedium,
      color: color,
      textAlign: textAlign,
    );
  }

  @override
  Widget build(BuildContext context) {
    return Text(
      text,
      style: (style ?? AppTypography.bodyMedium).copyWith(color: color),
      textAlign: textAlign,
      maxLines: maxLines,
      overflow: overflow,
    );
  }
}
