import React, { useState } from 'react';
import { Search, Filter, BookOpen, Compass, Plus } from 'lucide-react';
import { useLumina } from '../../context/LuminaContext';
import { HeaderBar } from '../navigation/HeaderBar';

export const LibraryScreen: React.FC = () => {
  const { mangas, navigate } = useLumina();
  const [selectedCategory, setSelectedCategory] = useState<'All' | 'Reading' | 'Completed' | 'On Hold'>('All');
  const [search, setSearch] = useState('');

  const categories: ('All' | 'Reading' | 'Completed' | 'On Hold')[] = ['All', 'Reading', 'Completed', 'On Hold'];

  // Filter manga
  const filteredMangas = mangas.filter(m => {
    // Only show inLibrary items unless user selected All and library is empty
    if (!m.inLibrary && selectedCategory !== 'All') return false;

    const matchesSearch =
      search.trim() === '' ||
      m.title.toLowerCase().includes(search.toLowerCase()) ||
      m.author.toLowerCase().includes(search.toLowerCase()) ||
      m.genre.some(g => g.toLowerCase().includes(search.toLowerCase()));

    const matchesCategory =
      selectedCategory === 'All'
        ? true
        : selectedCategory === 'Reading'
        ? m.id % 2 === 1
        : selectedCategory === 'Completed'
        ? m.id % 2 === 0
        : m.id === 4;

    return matchesSearch && matchesCategory;
  });

  return (
    <div className="min-h-screen bg-black text-white pb-24">
      <HeaderBar />

      <main className="max-w-xl mx-auto px-4 pt-3 space-y-4">
        {/* Search Bar & Filter */}
        <div className="flex items-center gap-2">
          <div className="relative flex-1">
            <Search className="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-zinc-500" />
            <input
              type="text"
              value={search}
              onChange={e => setSearch(e.target.value)}
              placeholder="Search library, authors, genres..."
              className="w-full bg-[#121216] border border-[#27272A] focus:border-[#8B5CF6] rounded-xl pl-10 pr-4 py-2 text-sm text-white placeholder-zinc-500 outline-none transition-colors"
            />
          </div>
          <button className="w-9 h-9 rounded-xl bg-[#121216] border border-[#27272A] flex items-center justify-center text-zinc-400 hover:text-white">
            <Filter className="w-4 h-4" />
          </button>
        </div>

        {/* Category Pills */}
        <div className="flex items-center gap-2 overflow-x-auto pb-1 no-scrollbar">
          {categories.map(cat => {
            const isSelected = selectedCategory === cat;
            return (
              <button
                key={cat}
                onClick={() => setSelectedCategory(cat)}
                className={`px-4 py-1.5 rounded-full text-xs font-bold whitespace-nowrap transition-all ${
                  isSelected
                    ? 'bg-[#8B5CF6]/30 border border-[#A78BFA] text-white shadow-[0_0_12px_rgba(139,92,246,0.3)]'
                    : 'bg-[#121216] border border-[#27272A] text-zinc-400 hover:text-zinc-200'
                }`}
              >
                {cat}
              </button>
            );
          })}
        </div>

        {/* Grid of Manga */}
        {filteredMangas.length === 0 ? (
          <div className="py-16 text-center space-y-3">
            <div className="w-12 h-12 mx-auto rounded-2xl bg-[#121216] border border-[#27272A] flex items-center justify-center text-zinc-500">
              <BookOpen className="w-6 h-6" />
            </div>
            <h3 className="text-sm font-bold text-zinc-300">No manga found</h3>
            <p className="text-xs text-zinc-500 max-w-xs mx-auto">
              Explore the global catalog to add manga series to your library.
            </p>
            <button
              onClick={() => navigate('explore')}
              className="inline-flex items-center gap-2 px-4 py-2 rounded-xl bg-[#8B5CF6] hover:bg-[#7C3AED] text-white text-xs font-bold shadow-lg"
            >
              <Compass className="w-4 h-4" />
              Explore Manga Catalog
            </button>
          </div>
        ) : (
          <div className="grid grid-cols-2 sm:grid-cols-3 gap-3">
            {filteredMangas.map(manga => {
              const newBadge = manga.id === 1 ? '3 New' : manga.id === 3 ? '12 New' : null;
              const progress = manga.id === 1 ? 0.75 : manga.id === 2 ? 1.0 : manga.id === 3 ? 0.4 : 0.2;
              const progressColor = manga.id === 2 ? 'bg-[#10B981]' : 'bg-[#8B5CF6]';
              const subtitle =
                manga.id === 1
                  ? 'Ch. 142 • 5d ago'
                  : manga.id === 2
                  ? 'Ch. 56 • Read'
                  : manga.id === 3
                  ? 'Ch. 21 • 2w ago'
                  : 'Ch. 8 • On Hold';

              return (
                <div
                  key={manga.id}
                  onClick={() => navigate('detail', manga.id)}
                  className="group relative bg-[#121216] border border-[#27272A] hover:border-[#8B5CF6]/60 rounded-2xl overflow-hidden cursor-pointer flex flex-col transition-all duration-200"
                >
                  <div className="relative aspect-[3/4] overflow-hidden bg-zinc-900">
                    <img
                      src={manga.thumbnailUrl}
                      alt={manga.title}
                      className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300"
                    />
                    <div className="absolute inset-0 bg-gradient-to-t from-black via-black/30 to-transparent" />

                    {/* New Chapters Badge */}
                    {newBadge && (
                      <span className="absolute top-2 right-2 px-2 py-0.5 rounded-lg bg-[#8B5CF6] border border-[#A78BFA] text-[10px] font-extrabold text-white shadow-md">
                        {newBadge}
                      </span>
                    )}
                  </div>

                  <div className="p-3 flex flex-col flex-1 justify-between gap-2">
                    <div>
                      <h4 className="text-xs font-bold text-white line-clamp-2 group-hover:text-[#A78BFA] transition-colors leading-tight">
                        {manga.title}
                      </h4>
                      <p className="text-[11px] text-zinc-400 mt-1">{subtitle}</p>
                    </div>

                    {/* Progress Bar */}
                    <div className="w-full bg-[#27272A] h-1 rounded-full overflow-hidden">
                      <div
                        className={`h-full ${progressColor} rounded-full`}
                        style={{ width: `${Math.round(progress * 100)}%` }}
                      />
                    </div>
                  </div>
                </div>
              );
            })}
          </div>
        )}
      </main>
    </div>
  );
};
