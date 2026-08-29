export type ScreenType =
  | 'home'
  | 'library'
  | 'explore'
  | 'updates'
  | 'more'
  | 'detail'
  | 'reader'
  | 'universe'
  | 'dna'
  | 'forge'
  | 'mutation'
  | 'ai'
  | 'journey'
  | 'timeline'
  | 'backup'
  | 'settings'
  | 'repositories';

export interface ScreenState {
  type: ScreenType;
  mangaId?: number;
  chapterId?: number;
  initialPage?: number;
}

export enum ReaderMode {
  CONTINUOUS_WEBTOON = 'CONTINUOUS_WEBTOON',
  SINGLE_PAGE_LTR = 'SINGLE_PAGE_LTR',
  SINGLE_PAGE_RTL = 'SINGLE_PAGE_RTL',
  DUAL_PAGE_SPREAD = 'DUAL_PAGE_SPREAD',
}

export enum BackgroundTint {
  PITCH_BLACK = 'PITCH_BLACK',
  CHARCOAL = 'CHARCOAL',
  SEPIA = 'SEPIA',
  DEEP_SLATE = 'DEEP_SLATE',
}

export interface ReaderConfig {
  readerMode: ReaderMode;
  backgroundTint: BackgroundTint;
  enableLuminaFlow: boolean;
  keepScreenOn: boolean;
  wifiOnlyDownload: boolean;
  hardwareAcceleration: boolean;
  zoomLevel: number;
  brightness: number;
}

export interface Manga {
  id: number;
  title: string;
  author: string;
  artist: string;
  description: string;
  genre: string[];
  status: string;
  thumbnailUrl: string;
  bannerUrl: string;
  inLibrary: boolean;
  rating: number;
  source: string;
  totalChapters: number;
  latestChapter: string;
  unreadCount: number;
}

export interface Chapter {
  id: number;
  mangaId: number;
  chapterNumber: number;
  title: string;
  scanlator: string;
  dateUpload: string;
  isRead: boolean;
  isDownloaded: boolean;
  pageCount: number;
  pages: string[];
}

export interface ReadingSnap {
  mangaId: number;
  title: string;
  coverUrl: string;
  chapterId: number;
  chapterNumber: number;
  pageIndex: number;
  totalPages: number;
  progressPercent: number;
  timestamp: number;
}

export interface UniverseNode {
  id: string;
  name: string;
  role: string;
  powerLevel: string;
  faction: string;
  bio: string;
  color: string;
  x: number;
  y: number;
}

export interface UniverseEdge {
  fromNodeId: string;
  toNodeId: string;
  relationLabel: string;
  color: string;
}

export interface DnaAttribute {
  trait: string;
  intensity: number;
  description: string;
  color: string;
}

export interface Achievement {
  id: string;
  title: string;
  desc: string;
  xp: number;
  unlocked: boolean;
}

export interface ExperiencePack {
  id: string;
  title: string;
  genre: string;
  isActive: boolean;
  isDraft: boolean;
  readingDirection: string;
  hapticIntensity: number;
  backgroundTheme: string;
  audioProfile: string;
  description: string;
}

export interface MutationBehavior {
  id: string;
  title: string;
  type: string;
  description: string;
  isLocked: boolean;
}

export interface AiChatMessage {
  id: string;
  text: string;
  isFromUser: boolean;
  timestamp: number;
  recommendations?: AiRecommendationCard[];
}

export interface AiRecommendationCard {
  id: string;
  title: string;
  coverUrl: string;
  tag1: string;
  tag2: string;
  description: string;
  matchScore: number;
}

export interface MangaUpdateItem {
  id: string;
  mangaId: number;
  title: string;
  chapterDisplay: string;
  coverUrl: string;
  timeAgo: string;
  isDownloaded: boolean;
}

export interface ExtensionPackage {
  name: string;
  packageName: string;
  versionName: string;
  versionCode: number;
  lang: string;
  apk: string;
  icon: string;
  installed?: boolean;
}

export interface TimelineItem {
  id: string;
  title: string;
  arcBadge: string;
  description: string;
  isMajorEvent: boolean;
  imageUrl?: string;
  dateOrChapter: string;
}
