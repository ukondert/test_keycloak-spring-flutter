import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:go_router/go_router.dart';
import '../../../../design_system/design_tokens.dart';
import '../../../../design_system/components/atoms/app_button.dart';
import '../../../../design_system/components/atoms/app_text.dart';
import '../providers/auth_provider.dart';

/// Welcome Page (Protected Route)
/// Displays user information and provides logout functionality
class WelcomePage extends StatelessWidget {
  const WelcomePage({super.key});

  Future<void> _handleLogout(BuildContext context) async {
    final authProvider = context.read<AuthProvider>();
    await authProvider.logout();

    if (!context.mounted) return;
    context.go('/login');
  }

  @override
  Widget build(BuildContext context) {
    final authProvider = context.watch<AuthProvider>();
    final user = authProvider.currentUser;

    return Scaffold(
      backgroundColor: AppColors.background,
      appBar: AppBar(
        title: const Text('Welcome'),
        backgroundColor: AppColors.primary,
        foregroundColor: AppColors.textOnPrimary,
        elevation: 0,
        actions: [
          IconButton(
            icon: const Icon(Icons.logout),
            onPressed: () => _handleLogout(context),
            tooltip: 'Logout',
          ),
        ],
      ),
      body: SafeArea(
        child: user == null
            ? const Center(
                child: CircularProgressIndicator(),
              )
            : Center(
                child: SingleChildScrollView(
                  padding: const EdgeInsets.all(AppSpacing.l),
                  child: ConstrainedBox(
                    constraints: const BoxConstraints(maxWidth: 500),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.center,
                      children: [
                        Container(
                          width: 120,
                          height: 120,
                          decoration: BoxDecoration(
                            color: AppColors.primaryLight,
                            shape: BoxShape.circle,
                          ),
                          child: Center(
                            child: Text(
                              user.firstName[0].toUpperCase() +
                                  user.lastName[0].toUpperCase(),
                              style: AppTypography.displayMedium.copyWith(
                                color: AppColors.textOnPrimary,
                                fontWeight: FontWeight.bold,
                              ),
                            ),
                          ),
                        ),
                        const SizedBox(height: AppSpacing.l),
                        AppText.headline(
                          'Welcome, ${user.firstName}!',
                          textAlign: TextAlign.center,
                          color: AppColors.textPrimary,
                        ),
                        const SizedBox(height: AppSpacing.s),
                        AppText.body(
                          'You are successfully authenticated',
                          textAlign: TextAlign.center,
                          color: AppColors.textSecondary,
                        ),
                        const SizedBox(height: AppSpacing.xl),
                        _UserInfoCard(user: user),
                        const SizedBox(height: AppSpacing.xl),
                        AppButton(
                          text: 'Logout',
                          onPressed: () => _handleLogout(context),
                          isLoading: authProvider.isLoading,
                          icon: Icons.logout,
                        ),
                      ],
                    ),
                  ),
                ),
              ),
      ),
    );
  }
}

/// Organism: User Info Card
/// Displays user information in a card format
class _UserInfoCard extends StatelessWidget {
  final dynamic user;

  const _UserInfoCard({required this.user});

  @override
  Widget build(BuildContext context) {
    return Card(
      elevation: AppElevation.medium,
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(AppBorderRadius.m),
      ),
      child: Padding(
        padding: const EdgeInsets.all(AppSpacing.l),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            AppText.title(
              'User Information',
              color: AppColors.textPrimary,
            ),
            const SizedBox(height: AppSpacing.m),
            const Divider(),
            const SizedBox(height: AppSpacing.m),
            _InfoRow(
              icon: Icons.person_outline,
              label: 'Username',
              value: user.username,
            ),
            const SizedBox(height: AppSpacing.m),
            _InfoRow(
              icon: Icons.email_outlined,
              label: 'Email',
              value: user.email,
            ),
            const SizedBox(height: AppSpacing.m),
            _InfoRow(
              icon: Icons.badge_outlined,
              label: 'Full Name',
              value: user.fullName,
            ),
            const SizedBox(height: AppSpacing.m),
            _InfoRow(
              icon: Icons.fingerprint,
              label: 'User ID',
              value: user.id,
            ),
          ],
        ),
      ),
    );
  }
}

/// Molecule: Info Row
/// Displays a labeled piece of information with an icon
class _InfoRow extends StatelessWidget {
  final IconData icon;
  final String label;
  final String value;

  const _InfoRow({
    required this.icon,
    required this.label,
    required this.value,
  });

  @override
  Widget build(BuildContext context) {
    return Row(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Icon(
          icon,
          size: 20,
          color: AppColors.primary,
        ),
        const SizedBox(width: AppSpacing.s),
        Expanded(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              AppText.label(
                label,
                color: AppColors.textSecondary,
              ),
              const SizedBox(height: AppSpacing.xs),
              AppText.body(
                value,
                color: AppColors.textPrimary,
              ),
            ],
          ),
        ),
      ],
    );
  }
}
