import React from 'react';
import { BookOpen, Bell, History, Compass, MoreHorizontal, Sparkles } from 'lucide-react';
import { useLumina } from '../../context/LuminaContext';
import { ScreenType } from '../../types';

export const BottomNavBar: React.FC = () => {
  const { screen, navigate, updates, historyItems } = useLumina();

  const navItems: { type: ScreenType; label: string; icon: React.ComponentType<{ className?: string }> }[] = [
    { type: 'library', label: 'Library', icon: BookOpen },
    { type: 'updates', label: 'Updates', icon: Bell },
    { type: 'history', label: 'History', icon: History },
    { type: 'explore', label: 'Browse', icon: Compass },
    { type: 'more', label: 'More', icon: MoreHorizontal },
  ];

  const unreadUpdatesCount = updates.filter(u => !u.isDownloaded).length;

  return (
    <nav className="fixed bottom-0 left-0 right-0 z-40 bg-[#0F0F14]/95 backdrop-blur-xl border-t border-[#22222D] px-2 py-1 sm:py-2">
      <div className="max-w-md mx-auto flex items-center justify-around">
        {navItems.map(item => {
          const isSelected = screen.type === item.type;
          const Icon = item.icon;

          return (
            <button
              key={item.type}
              onClick={() => navigate(item.type)}
              className={`relative flex flex-col items-center justify-center py-1 px-3 rounded-xl transition-all duration-200 ${
                isSelected
                  ? 'text-[#A78BFA] scale-105'
                  : 'text-zinc-400 hover:text-zinc-200'
              }`}
            >
              {isSelected && (
                <div className="absolute -top-1 w-6 h-1 rounded-full bg-[#8B5CF6] shadow-[0_0_10px_#8B5CF6]" />
              )}

              <div className="relative">
                <Icon className={`w-5 h-5 transition-transform ${isSelected ? 'stroke-[2.5px]' : 'stroke-2'}`} />
                {item.type === 'updates' && unreadUpdatesCount > 0 && (
                  <span className="absolute -top-1 -right-1.5 w-2 h-2 rounded-full bg-[#00E5FF] shadow-[0_0_6px_#00E5FF]" />
                )}
              </div>

              <span className={`text-[11px] mt-0.5 tracking-tight font-medium ${isSelected ? 'text-white font-bold' : 'text-zinc-400'}`}>
                {item.label}
              </span>
            </button>
          );
        })}
      </div>
    </nav>
  );
};
