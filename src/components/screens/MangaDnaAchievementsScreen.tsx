import React from 'react';
import {
  ArrowLeft,
  Sparkles,
  Flame,
  Zap,
  Clock,
  Award,
  Lock,
  CheckCircle2,
  TrendingUp,
  BarChart,
} from 'lucide-react';
import { useLumina } from '../../context/LuminaContext';

export const MangaDnaAchievementsScreen: React.FC = () => {
  const {
    dnaAttributes,
    achievements,
    readingSpeedPpm,
    readingStreakDays,
    totalReadingMinutes,
    goBack,
  } = useLumina();

  const unlockedCount = achievements.filter(a => a.unlocked).length;
  const totalXp = achievements.filter(a => a.unlocked).reduce((sum, a) => sum + a.xp, 0);

  return (
    <div className="min-h-screen bg-black text-white pb-16">
      {/* Top Header */}
      <header className="sticky top-0 z-30 bg-black/90 backdrop-blur-md border-b border-[#27272A] px-4 py-3 flex items-center justify-between">
        <div className="flex items-center gap-3">
          <button
            onClick={goBack}
            className="w-9 h-9 rounded-full bg-[#121216] border border-[#27272A] flex items-center justify-center text-white hover:border-[#8B5CF6] transition-colors"
          >
            <ArrowLeft className="w-4 h-4" />
          </button>
          <div>
            <h2 className="text-sm font-black text-white flex items-center gap-2">
              Manga DNA & Intelligence
              <span className="text-[10px] bg-[#8B5CF6]/20 text-[#A78BFA] border border-[#8B5CF6]/40 px-1.5 py-0.5 rounded font-mono">
                Level 4
              </span>
            </h2>
            <p className="text-[11px] text-zinc-400">Narrative genome analysis & XP progress</p>
          </div>
        </div>
      </header>

      <main className="max-w-xl mx-auto px-4 pt-4 space-y-6">
        {/* VITALS CARDS */}
        <div className="grid grid-cols-3 gap-2.5">
          <div className="bg-[#121216] border border-[#27272A] rounded-2xl p-3.5 space-y-1">
            <div className="flex items-center gap-2 text-amber-400">
              <Flame className="w-4 h-4 fill-amber-400" />
              <span className="text-[11px] font-bold uppercase text-zinc-400">Streak</span>
            </div>
            <div className="text-lg font-black text-white">{readingStreakDays} Days</div>
            <div className="text-[10px] text-zinc-500 font-medium">Unbroken momentum</div>
          </div>

          <div className="bg-[#121216] border border-[#27272A] rounded-2xl p-3.5 space-y-1">
            <div className="flex items-center gap-2 text-[#00E5FF]">
              <Zap className="w-4 h-4 fill-[#00E5FF]" />
              <span className="text-[11px] font-bold uppercase text-zinc-400">Velocity</span>
            </div>
            <div className="text-lg font-black text-white">{readingSpeedPpm} PPM</div>
            <div className="text-[10px] text-zinc-500 font-medium">Pages per minute</div>
          </div>

          <div className="bg-[#121216] border border-[#27272A] rounded-2xl p-3.5 space-y-1">
            <div className="flex items-center gap-2 text-[#A78BFA]">
              <Clock className="w-4 h-4" />
              <span className="text-[11px] font-bold uppercase text-zinc-400">Time</span>
            </div>
            <div className="text-lg font-black text-white">{Math.round(totalReadingMinutes / 60)}h {totalReadingMinutes % 60}m</div>
            <div className="text-[10px] text-zinc-500 font-medium">Total immersion</div>
          </div>
        </div>

        {/* NARRATIVE GENOME PROFILE (Manga DNA Bars) */}
        <div className="space-y-3">
          <div className="flex items-center justify-between">
            <h3 className="text-xs font-extrabold uppercase tracking-wider text-[#00E5FF] flex items-center gap-1.5">
              <Sparkles className="w-4 h-4" />
              Narrative Genome Profile
            </h3>
            <span className="text-xs text-zinc-400 font-mono">Resonance 94%</span>
          </div>

          <div className="bg-[#121216] border border-[#27272A] rounded-2xl p-4 space-y-4">
            {dnaAttributes.map((attr, idx) => (
              <div key={idx} className="space-y-1.5">
                <div className="flex items-center justify-between text-xs">
                  <span className="font-bold text-white">{attr.trait}</span>
                  <span className="font-mono font-bold" style={{ color: attr.color }}>
                    {Math.round(attr.intensity * 100)}%
                  </span>
                </div>

                <div className="w-full bg-[#27272A] h-2 rounded-full overflow-hidden">
                  <div
                    className="h-full rounded-full transition-all duration-1000"
                    style={{
                      width: `${Math.round(attr.intensity * 100)}%`,
                      backgroundColor: attr.color,
                      boxShadow: `0 0 10px ${attr.color}80`,
                    }}
                  />
                </div>

                <p className="text-[11px] text-zinc-400 leading-snug">{attr.description}</p>
              </div>
            ))}
          </div>
        </div>

        {/* ACHIEVEMENTS & XP VAULT */}
        <div className="space-y-3">
          <div className="flex items-center justify-between">
            <h3 className="text-xs font-extrabold uppercase tracking-wider text-amber-400 flex items-center gap-1.5">
              <Award className="w-4 h-4" />
              Achievements ({unlockedCount}/{achievements.length})
            </h3>
            <span className="px-2 py-0.5 rounded bg-amber-500/20 text-amber-300 border border-amber-500/40 text-[10px] font-mono font-bold">
              {totalXp} XP EARNED
            </span>
          </div>

          <div className="space-y-2.5">
            {achievements.map((ach) => (
              <div
                key={ach.id}
                className={`bg-[#121216] border rounded-2xl p-3.5 flex items-center justify-between gap-3 ${
                  ach.unlocked
                    ? 'border-[#27272A] hover:border-[#8B5CF6]/40'
                    : 'border-[#27272A]/50 opacity-60'
                }`}
              >
                <div className="flex items-center gap-3.5 min-w-0 flex-1">
                  <div
                    className={`w-10 h-10 rounded-xl flex items-center justify-center border flex-shrink-0 ${
                      ach.unlocked
                        ? 'bg-amber-500/10 border-amber-500/40 text-amber-400'
                        : 'bg-zinc-900 border-zinc-800 text-zinc-600'
                    }`}
                  >
                    {ach.unlocked ? <CheckCircle2 className="w-5 h-5" /> : <Lock className="w-4 h-4" />}
                  </div>

                  <div className="min-w-0">
                    <h4 className="text-xs font-bold text-white truncate">{ach.title}</h4>
                    <p className="text-[11px] text-zinc-400 leading-snug mt-0.5">{ach.desc}</p>
                  </div>
                </div>

                <span className="text-xs font-mono font-bold text-amber-400 flex-shrink-0">
                  +{ach.xp} XP
                </span>
              </div>
            ))}
          </div>
        </div>
      </main>
    </div>
  );
};
